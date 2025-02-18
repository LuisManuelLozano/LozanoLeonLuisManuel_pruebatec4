package com.luis.agencia.service;

import com.luis.agencia.dto.RoomBookingDto;
import com.luis.agencia.dto.RoomBookingDetailDto;
import com.luis.agencia.mapper.RoomBookingMapper;
import com.luis.agencia.model.*;
import com.luis.agencia.repository.IRoomBookingRepository;
import com.luis.agencia.repository.IRoomRepository;
import com.luis.agencia.repository.IPassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Servicio que gestiona la lógica de reservas de habitaciones ({@link RoomBooking}).
 * Permite crear, actualizar, eliminar y consultar reservas, así como
 * validar la disponibilidad de habitaciones y la existencia de pasajeros.
 */
@Service
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "unused"})
public class RoomBookingService implements IRoomBookingService {

    // --------------------------------------------------------------------------------------
    // CONSTANTES PARA EVITAR DUPLICACIÓN DE LITERALES
    // --------------------------------------------------------------------------------------
    private static final String MSG_NOT_EXISTS = "no existe";

    /**
     * Repositorio para realizar operaciones CRUD sobre las reservas de habitación.
     * Inyectado por Spring.
     */
    @Autowired
    private IRoomBookingRepository roomBookingRepository;

    /**
     * Repositorio para acceder a la información de las habitaciones.
     * Inyectado por Spring.
     */
    @Autowired
    private IRoomRepository roomRepository;

    /**
     * Repositorio para acceder a la información de los pasajeros.
     * Inyectado por Spring.
     */
    @Autowired
    private IPassengerRepository passengerRepository;

    /**
     * Servicio para gestionar la lógica de pasajeros, inyectado por Spring.
     * Se utiliza para verificar la existencia de pasajeros y recuperarlos.
     */
    @Autowired
    private IPassengerService passengerService;

    /**
     * Servicio para gestionar la lógica de hoteles.
     * Se utiliza para actualizar los contadores de habitaciones disponibles.
     */
    @Autowired
    private HotelService hotelService;

    /**
     * Mapper para convertir entre entidades {@link RoomBooking} y DTOs
     * {@link RoomBookingDto} o {@link RoomBookingDetailDto}.
     */
    @Autowired
    private RoomBookingMapper roomBookingMapper;

    /**
     * Crea una nueva reserva de habitación.
     * Valida la disponibilidad de habitaciones en base al tipo (doble o simple),
     * fechas y destino. Asigna las habitaciones solicitadas y asocia los pasajeros a la reserva.
     * <p>
     * Además, se actualizan los contadores de disponibilidad de habitaciones en el Hotel
     * a través del HotelService.
     *
     * @param roomBookingDto DTO con la información de la reserva (fechas, destino, habitaciones solicitadas, etc.).
     * @return Un {@link RoomBookingDto} con los datos de la reserva creada, incluyendo el costo total.
     */
    @Override
    @Transactional
    public RoomBookingDto createRoomBooking(RoomBookingDto roomBookingDto) {
        // 1) Validar y asignar habitaciones
        List<Room> assignedDoubleRooms = validateAndAssignRooms(
                roomBookingDto.getDoubleRoomQ(),
                RoomType.DOUBLE,
                roomBookingDto.getDateFrom(),
                roomBookingDto.getDateTo(),
                roomBookingDto.getDestination().trim()
        );
        List<Room> assignedSingleRooms = validateAndAssignRooms(
                roomBookingDto.getSingleRoomQ(),
                RoomType.SINGLE,
                roomBookingDto.getDateFrom(),
                roomBookingDto.getDateTo(),
                roomBookingDto.getDestination().trim()
        );
        List<Room> assignedRooms = new ArrayList<>();
        assignedRooms.addAll(assignedDoubleRooms);
        assignedRooms.addAll(assignedSingleRooms);

        // 2) Verificar la existencia de los pasajeros
        List<Passenger> passengers = validatePassengers(roomBookingDto.getPassengersIds());

        // 3) Crear la reserva y calcular costo total
        RoomBooking booking = buildRoomBooking(roomBookingDto, passengers, assignedRooms);
        double totalCost = calcTotalCost(assignedDoubleRooms, assignedSingleRooms, roomBookingDto.getNights());

        // 4) Guardar la reserva
        RoomBooking savedBooking = roomBookingRepository.save(booking);

        // 5) Vincular habitaciones y pasajeros a la reserva
        linkPassengersAndRooms(savedBooking, assignedRooms, passengers);

        // 6) Actualizar contadores en Hotel
        updateHotelCountersForCreation(assignedDoubleRooms, assignedSingleRooms);

        // 7) Retornar DTO
        RoomBookingDto savedDto = roomBookingMapper.entityToDto(savedBooking);
        savedDto.setTotalCost(totalCost);
        return savedDto;
    }

    /**
     * Actualiza una reserva de habitación existente, cambiando las fechas, el destino,
     * el número de habitaciones solicitadas y los pasajeros asociados.
     * Se revierte primero el efecto de la reserva anterior (incrementando los contadores del Hotel)
     * y luego se aplican los nuevos decrementos según la nueva asignación.
     *
     * @param id             Identificador de la reserva a actualizar.
     * @param roomBookingDto DTO con la nueva información de la reserva.
     * @return Un {@link RoomBookingDto} con los datos de la reserva actualizada, incluyendo el nuevo costo total.
     */
    @Override
    @Transactional
    public RoomBookingDto updateRoomBooking(Long id, RoomBookingDto roomBookingDto) {
        // 1) Buscar la reserva y revertir contadores de la asignación anterior
        RoomBooking existingBooking = roomBookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La reserva con id " + id + " " + MSG_NOT_EXISTS));
        revertHotelCounters(existingBooking);

        // 2) Desvincular pasajeros y habitaciones previos
        unlinkPassengersAndRooms(existingBooking);

        // 3) Validar y asignar nuevas habitaciones
        List<Room> assignedDoubleRooms = validateAndAssignRooms(
                roomBookingDto.getDoubleRoomQ(),
                RoomType.DOUBLE,
                roomBookingDto.getDateFrom(),
                roomBookingDto.getDateTo(),
                roomBookingDto.getDestination().trim()
        );
        List<Room> assignedSingleRooms = validateAndAssignRooms(
                roomBookingDto.getSingleRoomQ(),
                RoomType.SINGLE,
                roomBookingDto.getDateFrom(),
                roomBookingDto.getDateTo(),
                roomBookingDto.getDestination().trim()
        );
        List<Room> newAssignedRooms = new ArrayList<>();
        newAssignedRooms.addAll(assignedDoubleRooms);
        newAssignedRooms.addAll(assignedSingleRooms);

        // 4) Verificar la existencia de los nuevos pasajeros
        List<Passenger> newPassengers = validatePassengers(roomBookingDto.getPassengersIds());

        // 5) Actualizar la reserva existente y calcular costo total
        existingBooking.setDateFrom(roomBookingDto.getDateFrom());
        existingBooking.setDateTo(roomBookingDto.getDateTo());
        existingBooking.setNights(roomBookingDto.getNights());
        existingBooking.setPeopleQ(roomBookingDto.getPeopleQ());
        existingBooking.setPassengers(newPassengers);
        existingBooking.setRooms(newAssignedRooms);
        double newTotalCost = calcTotalCost(assignedDoubleRooms, assignedSingleRooms, roomBookingDto.getNights());

        RoomBooking savedBooking = roomBookingRepository.save(existingBooking);

        // 6) Vincular nuevas habitaciones y pasajeros
        linkPassengersAndRooms(savedBooking, newAssignedRooms, newPassengers);

        // 7) Actualizar contadores en Hotel
        updateHotelCountersForCreation(assignedDoubleRooms, assignedSingleRooms);

        // 8) Retornar DTO
        RoomBookingDto savedDto = roomBookingMapper.entityToDto(savedBooking);
        savedDto.setTotalCost(newTotalCost);
        return savedDto;
    }

    /**
     * Elimina físicamente una reserva de habitación, desvinculando previamente
     * las habitaciones y los pasajeros asociados, y actualizando los contadores del Hotel
     * para liberar las habitaciones reservadas.
     *
     * @param id Identificador de la reserva a eliminar.
     */
    @Override
    @Transactional
    public void deleteRoomBooking(Long id) {
        RoomBooking booking = roomBookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La reserva con id " + id + " " + MSG_NOT_EXISTS));
        // 1) Liberar habitaciones (incrementar contadores)
        revertHotelCounters(booking);

        // 2) Desvincular pasajeros y habitaciones
        unlinkPassengersAndRooms(booking);

        // 3) Eliminar la reserva
        roomBookingRepository.delete(booking);
    }

    /**
     * Retorna una lista con los detalles de todas las reservas de habitación existentes.
     *
     * @return Lista de {@link RoomBookingDetailDto} con la información de cada reserva.
     */
    @Override
    @Transactional
    public List<RoomBookingDetailDto> listAllRoomBookings() {
        List<RoomBooking> bookings = roomBookingRepository.findAll();
        return bookings.stream()
                .map(roomBookingMapper::entityToDetailDto)
                .toList();
    }

    /**
     * Obtiene los detalles de una reserva de habitación específica por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return Un {@link RoomBookingDetailDto} con la información de la reserva, o null si no existe.
     */
    @Override
    @Transactional
    public RoomBookingDetailDto getRoomBookingDetailById(Long id) {
        RoomBooking booking = roomBookingRepository.findById(id).orElse(null);
        return roomBookingMapper.entityToDetailDto(booking);
    }

    // --------------------------------------------------------------------------------------
    // MÉTODOS PRIVADOS PARA REDUCIR COMPLEJIDAD Y REUTILIZAR LÓGICA
    // --------------------------------------------------------------------------------------

    /**
     * Valida que haya suficientes habitaciones de un tipo específico y las asigna.
     * Lanza excepción si no hay suficientes.
     *
     * @param requested   Cantidad de habitaciones requeridas.
     * @param roomType    Tipo de habitación (DOUBLE o SINGLE).
     * @param dateFrom    Fecha de inicio.
     * @param dateTo      Fecha de fin.
     * @param destination Lugar de destino.
     * @return Lista de habitaciones asignadas.
     */
    private List<Room> validateAndAssignRooms(int requested, RoomType roomType,
                                              LocalDate dateFrom, LocalDate dateTo,
                                              String destination) {

        if (requested <= 0) {
            return Collections.emptyList();
        }
        List<Room> availableRooms = roomRepository
                .findByRoomTypeAndDisponibilityDateFromLessThanEqualAndDisponibilityDateToGreaterThanEqualAndRoomBookingIsNull(
                        roomType, dateFrom, dateTo
                ).stream()
                .filter(r -> r.getHotel().getPlace().equalsIgnoreCase(destination))
                .toList();

        if (availableRooms.size() < requested) {
            throw new IllegalArgumentException(
                    String.format("No hay suficientes habitaciones %s disponibles en el destino %s.",
                            roomType, destination)
            );
        }
        return new ArrayList<>(availableRooms.subList(0, requested));
    }

    /**
     * Verifica la existencia de los pasajeros a partir de sus IDs.
     * Lanza excepción si alguno no existe.
     *
     * @param passengersIds Lista de IDs de pasajeros.
     * @return Lista de entidades Passenger.
     */
    private List<Passenger> validatePassengers(List<Long> passengersIds) {
        if (passengersIds == null || passengersIds.isEmpty()) {
            return Collections.emptyList();
        }
        return passengersIds.stream()
                .map(id -> {
                    Passenger p = passengerService.findPassengerById(id);
                    if (p == null) {
                        throw new IllegalArgumentException("El pasajero con id " + id + " " + MSG_NOT_EXISTS);
                    }
                    return p;
                })
                .toList();
    }

    /**
     * Construye un objeto RoomBooking con los datos del DTO, la lista de pasajeros y habitaciones asignadas.
     *
     * @param dto        Datos de la reserva.
     * @param passengers Lista de pasajeros validados.
     * @param rooms      Lista de habitaciones asignadas.
     * @return Entidad RoomBooking lista para persistir.
     */
    private RoomBooking buildRoomBooking(RoomBookingDto dto, List<Passenger> passengers, List<Room> rooms) {
        RoomBooking booking = new RoomBooking();
        booking.setDateFrom(dto.getDateFrom());
        booking.setDateTo(dto.getDateTo());
        booking.setNights(dto.getNights());
        booking.setPeopleQ(dto.getPeopleQ());
        booking.setPassengers(passengers);
        booking.setRooms(rooms);
        return booking;
    }

    /**
     * Calcula el costo total de la reserva en base a las habitaciones asignadas y el número de noches.
     *
     * @param doubleRooms Lista de habitaciones dobles.
     * @param singleRooms Lista de habitaciones simples.
     * @param nights      Número de noches.
     * @return Costo total.
     */
    private double calcTotalCost(List<Room> doubleRooms, List<Room> singleRooms, int nights) {
        double total = 0.0;
        for (Room room : doubleRooms) {
            total += room.getHotel().getDoubleRoomPrice();
        }
        for (Room room : singleRooms) {
            total += room.getHotel().getSimpleRoomPrice();
        }
        return total * nights;
    }

    /**
     * Vincula las habitaciones y pasajeros a la reserva persistida, guardando los cambios.
     *
     * @param booking   Reserva ya persistida.
     * @param rooms     Lista de habitaciones asignadas.
     * @param passengers Lista de pasajeros validados.
     */
    private void linkPassengersAndRooms(RoomBooking booking, List<Room> rooms, List<Passenger> passengers) {
        for (Room room : rooms) {
            room.setRoomBooking(booking);
            roomRepository.save(room);
        }
        for (Passenger p : passengers) {
            p.setRoomBooking(booking);
            passengerRepository.save(p);
        }
    }

    /**
     * Desvincula pasajeros y habitaciones de la reserva, guardando los cambios.
     *
     * @param booking Reserva de la que se desvincularán pasajeros y habitaciones.
     */
    private void unlinkPassengersAndRooms(RoomBooking booking) {
        if (booking.getPassengers() != null) {
            for (Passenger p : booking.getPassengers()) {
                p.setRoomBooking(null);
                passengerRepository.save(p);
            }
        }
        if (booking.getRooms() != null) {
            for (Room r : booking.getRooms()) {
                r.setRoomBooking(null);
                roomRepository.save(r);
            }
        }
    }

    /**
     * Actualiza los contadores del hotel para cada habitación asignada (dobles y simples).
     * Llama a HotelService para reducir la disponibilidad en consecuencia.
     *
     * @param assignedDoubleRooms Lista de habitaciones dobles asignadas.
     * @param assignedSingleRooms Lista de habitaciones simples asignadas.
     */
    private void updateHotelCountersForCreation(List<Room> assignedDoubleRooms, List<Room> assignedSingleRooms) {
        Map<Hotel, Integer> hotelDoubleDecrement = new HashMap<>();
        for (Room room : assignedDoubleRooms) {
            Hotel hotel = room.getHotel();
            hotelDoubleDecrement.put(hotel, hotelDoubleDecrement.getOrDefault(hotel, 0) + 1);
        }
        Map<Hotel, Integer> hotelSingleDecrement = new HashMap<>();
        for (Room room : assignedSingleRooms) {
            Hotel hotel = room.getHotel();
            hotelSingleDecrement.put(hotel, hotelSingleDecrement.getOrDefault(hotel, 0) + 1);
        }
        for (Map.Entry<Hotel, Integer> entry : hotelDoubleDecrement.entrySet()) {
            hotelService.decrementRoomAvailability(entry.getKey(), entry.getValue(), 0);
        }
        for (Map.Entry<Hotel, Integer> entry : hotelSingleDecrement.entrySet()) {
            hotelService.decrementRoomAvailability(entry.getKey(), 0, entry.getValue());
        }
    }

    /**
     * Revertir (incrementar) los contadores del hotel para todas las habitaciones
     * asociadas a la reserva actual, por ejemplo, antes de editar o eliminar.
     *
     * @param booking Reserva que se va a modificar o eliminar.
     */
    private void revertHotelCounters(RoomBooking booking) {
        if (booking.getRooms() == null) {
            return;
        }
        Map<Hotel, Integer> hotelDoubleIncrement = new HashMap<>();
        Map<Hotel, Integer> hotelSingleIncrement = new HashMap<>();
        for (Room room : booking.getRooms()) {
            Hotel hotel = room.getHotel();
            if (room.getRoomType() == RoomType.DOUBLE) {
                hotelDoubleIncrement.put(hotel, hotelDoubleIncrement.getOrDefault(hotel, 0) + 1);
            } else if (room.getRoomType() == RoomType.SINGLE) {
                hotelSingleIncrement.put(hotel, hotelSingleIncrement.getOrDefault(hotel, 0) + 1);
            }
        }
        for (Map.Entry<Hotel, Integer> entry : hotelDoubleIncrement.entrySet()) {
            hotelService.incrementRoomAvailability(entry.getKey(), entry.getValue(), 0);
        }
        for (Map.Entry<Hotel, Integer> entry : hotelSingleIncrement.entrySet()) {
            hotelService.incrementRoomAvailability(entry.getKey(), 0, entry.getValue());
        }
    }
}













