package com.luis.agencia.service;

import com.luis.agencia.dto.FlightBookingDetailDto;
import com.luis.agencia.dto.FlightBookingDto;
import com.luis.agencia.model.Flight;
import com.luis.agencia.model.FlightBooking;
import com.luis.agencia.model.Passenger;
import com.luis.agencia.repository.IFlightBookingRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Servicio que gestiona las operaciones de creación, edición y eliminación
 * de reservas de vuelo ({@link FlightBooking}), así como la vinculación con
 * pasajeros y la actualización de la disponibilidad de asientos.
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // El IDE no detecta la inyección en tiempo de compilación
public class FlightBookingService implements IFlightBookingService {

    /**
     * Servicio para gestionar la lógica de vuelos.
     * Asignado por Spring en tiempo de ejecución.
     */
    @Autowired
    private IFlightService flightService;

    /**
     * Repositorio para realizar operaciones CRUD sobre las reservas de vuelo.
     * Asignado por Spring en tiempo de ejecución.
     */
    @Autowired
    private IFlightBookingRepository flightBookingRepo;

    /**
     * Servicio para gestionar la lógica de pasajeros.
     * Asignado por Spring en tiempo de ejecución.
     */
    @Autowired
    private IPassengerService passengerService;

    /**
     * Crea una nueva reserva de vuelo a partir de los datos recibidos.
     *
     * @param dto DTO con la información de la reserva (fecha, asientos, pasajeros, etc.).
     * @return Un {@link FlightBookingDetailDto} con los datos finales de la reserva creada.
     */
    @Override
    @Transactional
    public FlightBookingDetailDto create(FlightBookingDto dto) {
        return saveOrUpdate(new FlightBooking(), dto, true);
    }

    /**
     * Edita una reserva de vuelo existente.
     *
     * @param id  Identificador de la reserva de vuelo que se desea editar.
     * @param dto DTO con los nuevos datos para actualizar la reserva.
     * @return Un {@link FlightBookingDetailDto} con los datos finales de la reserva editada.
     */
    @Override
    @Transactional
    public FlightBookingDetailDto edit(Long id, FlightBookingDto dto) {
        FlightBooking flightBooking = flightBookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La reserva de vuelo con el ID " + id + " no existe"));
        return saveOrUpdate(flightBooking, dto, false);
    }

    /**
     * Método interno para crear o editar una reserva de vuelo, asegurando la lógica de
     * validación de pasajeros, búsqueda de vuelos disponibles, disponibilidad de asientos, etc.
     *
     * @param flightBooking Entidad de la reserva (nueva o existente).
     * @param dto           DTO con los datos de la reserva.
     * @param isNew         Indica si se trata de una reserva nueva (true) o una edición (false).
     * @return Un {@link FlightBookingDetailDto} con los datos finales de la reserva.
     */
    @Transactional
    private FlightBookingDetailDto saveOrUpdate(FlightBooking flightBooking, FlightBookingDto dto, boolean isNew) {
        String origen = dto.getOrigin();
        String destino = dto.getDestination();
        FlightBookingDetailDto detailDto = new FlightBookingDetailDto();

        // 1. Validar existencia de pasajeros.
        List<Long> invalidPassengersIds = dto.getPassengersIds().stream()
                .filter(id -> !passengerService.existsById(id))
                // Reemplazamos Collectors.toList() por toList(), ya que no modificamos la lista posteriormente
                .toList();
        if (!invalidPassengersIds.isEmpty()) {
            throw new IllegalArgumentException("Los siguientes IDs de pasajeros no son válidos: " + invalidPassengersIds);
        }

        // Si estamos editando, primero devolvemos los asientos al vuelo original.
        if (!isNew) {
            Flight originalFlight = flightBooking.getFlight();
            originalFlight.setEconomySeatsQ(originalFlight.getEconomySeatsQ() + flightBooking.getPeopleQ());
            originalFlight.setBusinessSeatsQ(originalFlight.getBusinessSeatsQ() + flightBooking.getPeopleQ());
            originalFlight.getFlightBookings().remove(flightBooking);
            flightService.save(originalFlight);
        }

        // 2. Buscar vuelos en la fecha indicada.
        List<Flight> vuelosEnFecha = flightService.findByDateFromOrByDateTo(dto.getDate());
        if (vuelosEnFecha.isEmpty()) {
            throw new IllegalArgumentException("No hay vuelos disponibles en esas fechas");
        }

        // 3. Filtrar vuelos por origen y destino (o viceversa).
        List<Flight> vuelosOk = vuelosEnFecha.stream()
                .filter(vuelo ->
                        (vuelo.getOrigin().equalsIgnoreCase(origen) && vuelo.getDestination().equalsIgnoreCase(destino))
                                || (vuelo.getOrigin().equalsIgnoreCase(destino) && vuelo.getDestination().equalsIgnoreCase(origen))
                )
                // Reemplazamos Collectors.toList() por toList(), ya que no modificamos la lista posteriormente
                .toList();

        // Si no hay vuelos que coincidan en origen/destino, lanzamos excepción.
        if (vuelosOk.isEmpty()) {
            throw new IllegalArgumentException("Hay vuelos en esas fechas pero no coinciden origen y destino");
        }

        // 4. Asignar el primer vuelo encontrado, usando orElseThrow para evitar warning por Optional.get()
        Flight flight = vuelosOk.stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hay vuelos en esas fechas pero no coinciden origen y destino"));
        flightBooking.setFlight(flight);
        detailDto.setFlightId(flight.getId());

        // 5. Validar disponibilidad de asientos.
        if (flight.getEconomySeatsQ() < dto.getTouristSeats()) {
            throw new IllegalArgumentException("No hay suficientes asientos de turista disponibles");
        }
        if (flight.getBusinessSeatsQ() < dto.getBussinessSeats()) {
            throw new IllegalArgumentException("No hay suficientes asientos de clase business disponibles");
        }

        // 6. Asignar fecha y cantidad de personas.
        flightBooking.setDate(dto.getDate());
        detailDto.setDate(dto.getDate());
        flightBooking.setPeopleQ(dto.getPeopleQ());
        detailDto.setPeopleQ(dto.getPeopleQ());

        // 7. Asignar origen, destino, asientos y número de vuelo al DTO.
        detailDto.setOrigin(origen);
        detailDto.setDestination(destino);
        detailDto.setTouristSeats(dto.getTouristSeats());
        detailDto.setBussinessSeats(dto.getBussinessSeats());
        detailDto.setFlightNumber(flight.getFlightNumber());

        // 8. Calcular el costo total.
        detailDto.setTotalCost(
                flight.getBusinessSeatPrice() * detailDto.getBussinessSeats()
                        + flight.getEconomySeatPrice() * detailDto.getTouristSeats()
        );

        // 9. Asociar la lista de pasajeros a la reserva.
        List<Passenger> listaPasajeros = dto.getPassengersIds().stream()
                .map(passengerService::findPassengerById)
                // Reemplazamos Collectors.toList() por toList(), ya que no modificamos la lista posteriormente
                .toList();
        flightBooking.getPassengers().clear();
        flightBooking.getPassengers().addAll(listaPasajeros);
        for (Passenger p : listaPasajeros) {
            p.setFlightBooking(flightBooking);
        }
        detailDto.setPassengersIds(dto.getPassengersIds());

        // 10. Decrementar asientos disponibles en el vuelo.
        flight.setEconomySeatsQ(flight.getEconomySeatsQ() - dto.getTouristSeats());
        flight.setBusinessSeatsQ(flight.getBusinessSeatsQ() - dto.getBussinessSeats());

        // 11. Agregar la reserva a la lista del vuelo (inicializada con Hibernate).
        Hibernate.initialize(flight.getFlightBookings());
        flight.getFlightBookings().add(flightBooking);

        // 12. Persistir primero el vuelo (con la nueva reserva) y luego la reserva de vuelo.
        flightService.save(flight);
        flightBookingRepo.save(flightBooking);

        // 13. Actualizar (re-guardar) los pasajeros, ahora que flightBooking ya está persistido.
        for (Passenger p : listaPasajeros) {
            passengerService.save(p);
        }

        // 14. Establecer los campos adicionales en el DTO de detalle.
        detailDto.setId(flightBooking.getId());
        detailDto.setFlightName(flight.getName());

        return detailDto;
    }

    /**
     * Elimina una reserva de vuelo por su identificador.
     * Se encarga de desvincular pasajeros y actualizar el vuelo asociado.
     *
     * @param id Identificador de la reserva que se desea eliminar.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        FlightBooking flightBookingDel = flightBookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        // Desvincular pasajeros de la reserva.
        if (flightBookingDel.getPassengers() != null) {
            for (Passenger passenger : flightBookingDel.getPassengers()) {
                passenger.setFlightBooking(null);
                passengerService.save(passenger);
            }
        }

        // Remover la reserva de la lista del vuelo asociado.
        Flight flight = flightBookingDel.getFlight();
        if (flight != null) {
            flight.getFlightBookings().remove(flightBookingDel);
            flightService.save(flight);
        }

        // Eliminar la reserva.
        flightBookingRepo.delete(flightBookingDel);
    }

    /**
     * Busca una reserva de vuelo por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return La entidad {@link FlightBooking} encontrada.
     * @throws IllegalArgumentException si no se encuentra la reserva.
     */
    @Override
    @Transactional
    public FlightBooking findById(Long id) {
        return flightBookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La reserva de vuelo con el ID " + id + " no existe"));
    }

    /**
     * Retorna la lista de todas las reservas de vuelo.
     *
     * @return Lista de todas las reservas de vuelo.
     * @throws IllegalArgumentException si la lista está vacía.
     */
    @Override
    @Transactional
    public List<FlightBooking> findAll() {
        List<FlightBooking> listaReservas = flightBookingRepo.findAll();
        if (listaReservas.isEmpty()) {
            throw new IllegalArgumentException("La lista está vacía");
        }
        return listaReservas;
    }
}














