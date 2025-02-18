package com.luis.agencia.service;

import com.luis.agencia.dto.RoomDto;
import com.luis.agencia.mapper.RoomMapper;
import com.luis.agencia.model.Room;
import com.luis.agencia.model.RoomType;
import com.luis.agencia.repository.IHotelRepository;
import com.luis.agencia.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que gestiona la lógica relacionada con las habitaciones ({@link Room}).
 * Permite crear, actualizar, eliminar y consultar habitaciones disponibles
 * según distintos criterios (fechas, hotel, destino, tipo de habitación).
 */
@Service
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "unused"})
public class RoomService implements IroomService {

    /**
     * Repositorio para acceder a la información de las habitaciones.
     * Inyectado por Spring.
     */
    @Autowired
    private IRoomRepository roomRepository;

    /**
     * Repositorio para acceder a la información de los hoteles,
     * utilizado para verificar la existencia de un hotel antes de crear una habitación.
     * Inyectado por Spring.
     */
    @Autowired
    private IHotelRepository hotelRepository;

    /**
     * Mapper para convertir entre entidades {@link Room} y DTOs {@link RoomDto}.
     * Inyectado por Spring.
     */
    @Autowired
    private RoomMapper roomMapper;

    /**
     * Retorna una lista de habitaciones disponibles en un hotel específico, según un rango de fechas.
     *
     * @param hotelId  Identificador del hotel.
     * @param fromDate Fecha de inicio de la disponibilidad.
     * @param toDate   Fecha de fin de la disponibilidad.
     * @return Lista de {@link RoomDto} que cumplen con el criterio.
     */
    @Override
    @Transactional
    public List<RoomDto> findAvailableRooms(Long hotelId, LocalDate fromDate, LocalDate toDate) {
        List<Room> rooms = roomRepository
                .findByHotelIdAndDisponibilityDateFromLessThanEqualAndDisponibilityDateToGreaterThanEqual(
                        hotelId, fromDate, toDate);
        return rooms.stream()
                .map(roomMapper::entityToDto)
                .toList();
    }

    /**
     * Crea una nueva habitación asociada a un hotel existente.
     *
     * @param roomDto DTO con la información de la habitación.
     * @return Mensaje indicando el resultado de la operación.
     */
    @Override
    @Transactional
    public String createRoom(RoomDto roomDto) {
        if (roomDto.getHotelId() == null) {
            return "El ID del hotel es obligatorio.";
        }
        if (!hotelRepository.existsById(roomDto.getHotelId())) {
            return "El hotel no existe.";
        }
        Room room = roomMapper.dtoToEntity(roomDto);
        roomRepository.save(room);
        return "Room created successfully.";
    }

    /**
     * Actualiza los datos de una habitación existente.
     *
     * @param roomId  Identificador de la habitación a actualizar.
     * @param roomDto DTO con la información nueva de la habitación.
     * @return Mensaje indicando el resultado de la operación.
     */
    @Override
    @Transactional
    public String updateRoom(Long roomId, RoomDto roomDto) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return "La habitación no existe.";
        }
        room.setRoomType(roomDto.getRoomType());
        room.setDisponibilityDateFrom(roomDto.getDisponibilityDateFrom());
        room.setDisponibilityDateTo(roomDto.getDisponibilityDateTo());
        roomRepository.save(room);
        return "Room updated successfully.";
    }

    /**
     * Elimina una habitación de la base de datos.
     *
     * @param roomId Identificador de la habitación a eliminar.
     * @return Mensaje indicando el resultado de la operación.
     */
    @Override
    @Transactional
    public String deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            return "La habitación no existe.";
        }
        roomRepository.deleteById(roomId);
        return "Room deleted successfully.";
    }

    /**
     * Busca una habitación por su identificador y la retorna en formato DTO.
     *
     * @param roomId Identificador de la habitación.
     * @return Un {@link RoomDto} con los datos de la habitación, o null si no existe.
     */
    @Override
    @Transactional
    public RoomDto findRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return null;
        }
        return roomMapper.entityToDto(room);
    }

    /**
     * Retorna una lista de todas las habitaciones en la base de datos, en formato DTO.
     *
     * @return Lista de {@link RoomDto}.
     */
    @Override
    @Transactional
    public List<RoomDto> listAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::entityToDto)
                .toList();
    }

    /**
     * Retorna la lista de habitaciones asociadas a un hotel en particular.
     *
     * @param hotelId Identificador del hotel.
     * @return Lista de {@link RoomDto} de ese hotel.
     */
    @Override
    @Transactional
    public List<RoomDto> listRoomsByHotel(Long hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms.stream()
                .map(roomMapper::entityToDto)
                .toList();
    }

    /**
     * Retorna una lista de habitaciones disponibles según el destino y un rango de fechas.
     *
     * @param destination Destino (lugar) del hotel.
     * @param fromDate    Fecha de inicio de la disponibilidad.
     * @param toDate      Fecha de fin de la disponibilidad.
     * @return Lista de {@link RoomDto} que cumplen con los criterios.
     */
    @Override
    @Transactional
    public List<RoomDto> findAvailableRoomsByDestination(String destination, LocalDate fromDate, LocalDate toDate) {
        List<Room> rooms = roomRepository.findAvailableRoomsByDestination(destination, fromDate, toDate);
        return rooms.stream()
                .map(roomMapper::entityToDto)
                .toList();
    }

    /**
     * Retorna una lista de habitaciones disponibles según el tipo de habitación, el destino
     * y un rango de fechas. Solo incluye aquellas habitaciones que no estén ya reservadas.
     *
     * @param roomType    Tipo de habitación (ej. SINGLE, DOUBLE).
     * @param fromDate    Fecha de inicio de la disponibilidad.
     * @param toDate      Fecha de fin de la disponibilidad.
     * @param destination Destino (lugar) del hotel.
     * @return Lista de {@link RoomDto} que cumplen con los criterios.
     */
    @Override
    @Transactional
    public List<RoomDto> findAvailableRoomsByTypeAndDestination(String roomType, LocalDate fromDate, LocalDate toDate, String destination) {
        // Convertir el String a enum RoomType
        RoomType type = RoomType.valueOf(roomType.toUpperCase());

        List<Room> rooms = roomRepository
                .findByRoomTypeAndDisponibilityDateFromLessThanEqualAndDisponibilityDateToGreaterThanEqualAndRoomBookingIsNull(
                        type, fromDate, toDate
                ).stream()
                .filter(r -> r.getHotel().getPlace().equalsIgnoreCase(destination))
                .toList();

        return rooms.stream()
                .map(roomMapper::entityToDto)
                .toList();
    }
}


