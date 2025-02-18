package com.luis.agencia.service;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.mapper.FlightMapper;
import com.luis.agencia.model.Flight;
import com.luis.agencia.repository.IFlightRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica relacionada con los vuelos, incluyendo
 * creación, edición, eliminación y obtención de vuelos disponibles.
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // El IDE no detecta la inyección en tiempo de compilación
public class FlightService implements IFlightService {

    /**
     * Repositorio para acceder a la información de los vuelos en la base de datos.
     * Inyectado por Spring.
     */
    @Autowired
    private IFlightRepository flightRepository;

    /**
     * Mapper para convertir entre entidades {@link Flight} y DTOs {@link FlightDto}.
     * Inyectado por Spring.
     */
    @Autowired
    private FlightMapper flightMapper;

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    /**
     * Busca un vuelo por su identificador.
     *
     * @param id Identificador del vuelo.
     * @return El vuelo si se encuentra, o null en caso contrario.
     */
    public Flight findFlightById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.orElse(null);
    }

    /**
     * Busca un vuelo por su identificador y lo devuelve en forma de DTO.
     *
     * @param id Identificador del vuelo.
     * @return El DTO del vuelo si se encuentra, o null en caso contrario.
     */
    public FlightDto findFlightDtoById(Long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.map(flightMapper::entityToDto).orElse(null);
    }

    /**
     * Crea un nuevo vuelo en la base de datos a partir de un DTO.
     *
     * @param flightDto DTO con la información del vuelo a crear.
     * @throws IllegalArgumentException si el DTO es nulo o si ya existe un vuelo con el mismo número.
     */
    @Transactional
    public void createFlight(FlightDto flightDto) {
        if (flightDto == null) {
            throw new IllegalArgumentException("Los datos del vuelo no pueden ser nulos.");
        }

        if (flightRepository.findByFlightNumber(flightDto.getFlightNumber()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vuelo con ese número.");
        }

        flightRepository.save(flightMapper.dtoToEntity(flightDto));
    }

    /**
     * Edita un vuelo existente en la base de datos.
     *
     * @param id        Identificador del vuelo que se desea editar.
     * @param flightDto DTO con la información actualizada del vuelo.
     * @return Mensaje indicando el resultado de la operación.
     */
    @Transactional
    public String editFlight(Long id, FlightDto flightDto) {
        logger.debug("editFlight llamado en FlightService con ID: {}", id);
        Optional<Flight> flightOpt = flightRepository.findById(id);

        if (flightOpt.isEmpty()) {
            logger.debug("Vuelo no encontrado con ID: {}", id);
            return "El vuelo a editar no ha sido encontrado";
        }

        Optional<Flight> flightWithSameNumber = flightRepository.findByFlightNumber(flightDto.getFlightNumber());
        if (flightWithSameNumber.isPresent() && !flightWithSameNumber.get().getId().equals(id)) {
            logger.debug("El número de vuelo ya existe para otro vuelo: {}", flightDto.getFlightNumber());
            return "El número de vuelo ya existe para otro vuelo";
        }

        Flight flightAnt = flightOpt.get();
        if (!flightAnt.isActiva()) {
            logger.debug("El vuelo no está activo y no puede ser modificado: {}", id);
            return "El vuelo no puede ser modificado porque no está activo";
        }

        Flight flightNew = flightMapper.dtoToEntity(flightDto);
        logger.debug("Editando vuelo con ID: {}", id);

        // Se actualizan solo los campos necesarios del vuelo existente
        flightAnt.setName(flightNew.getName());
        flightAnt.setFlightNumber(flightNew.getFlightNumber());
        flightAnt.setDateFrom(flightNew.getDateFrom());
        flightAnt.setDateTo(flightNew.getDateTo());
        flightAnt.setOrigin(flightNew.getOrigin());
        flightAnt.setDestination(flightNew.getDestination());
        flightAnt.setBusinessSeatsQ(flightNew.getBusinessSeatsQ());
        flightAnt.setEconomySeatsQ(flightNew.getEconomySeatsQ());
        flightAnt.setBusinessSeatPrice(flightNew.getBusinessSeatPrice());
        flightAnt.setEconomySeatPrice(flightNew.getEconomySeatPrice());

        flightRepository.save(flightAnt);
        logger.debug("El vuelo ha sido editado con éxito con ID: {}", id);
        return "El vuelo ha sido editado con éxito";
    }

    /**
     * Retorna una lista de todos los vuelos activos en forma de DTO.
     *
     * @return Lista de DTOs de vuelos que están activos.
     */
    public List<FlightDto> listFlights() {
        return flightRepository.findAll().stream()
                .map(flightMapper::entityToDto)   // Reemplazo de lambda por referencia de método
                .filter(FlightDto::isActiva)      // Reemplazo de lambda por referencia de método
                .toList();
    }

    /**
     * Marca un vuelo como inactivo (eliminación lógica), siempre que no tenga reservas pendientes.
     *
     * @param id Identificador del vuelo a eliminar.
     * @return Mensaje indicando el resultado de la operación.
     */
    @Transactional
    public String deleteFlight(Long id) {
        Optional<Flight> vueloOpt = flightRepository.findById(id);
        if (vueloOpt.isPresent()) {
            Flight flight = vueloOpt.get();
            if (flight.getFlightBookings().isEmpty()) {
                flight.setActiva(false);
                flightRepository.save(flight);
                return "El vuelo ha sido eliminado por lógica";
            } else {
                return "El vuelo no ha sido eliminado porque tiene reservas pendientes";
            }
        }
        return "El vuelo que pretende eliminar no existe";
    }

    /**
     * Retorna una lista de vuelos disponibles en base a la fecha de salida, fecha de llegada,
     * origen y destino.
     *
     * @param dateFrom    Fecha de salida.
     * @param dateTo      Fecha de llegada.
     * @param origin      Origen del vuelo.
     * @param destination Destino del vuelo.
     * @return Lista de vuelos disponibles que coinciden con los parámetros proporcionados.
     */
    @Override
    public List<Flight> getVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo,
                                             String origin, String destination) {
        List<Flight> vuelosIda = flightRepository.findByDateFromAndOriginAndDestination(dateFrom, origin, destination);
        List<Flight> vuelosVuelta = flightRepository.findByDateToAndOriginAndDestination(dateTo, destination, origin);

        List<Flight> vuelosDisponibles = new ArrayList<>();
        vuelosDisponibles.addAll(vuelosIda);
        vuelosDisponibles.addAll(vuelosVuelta);

        return vuelosDisponibles;
    }

    /**
     * Retorna una lista de vuelos que coinciden con la fecha de salida o la fecha de llegada proporcionada.
     *
     * @param date Fecha a comparar.
     * @return Lista de vuelos que tienen la fecha indicada en la fecha de salida o llegada.
     */
    @Override
    public List<Flight> findByDateFromOrByDateTo(LocalDate date) {
        return flightRepository.findByDateFromOrDateTo(date);
    }

    /**
     * Guarda o actualiza un vuelo en la base de datos.
     *
     * @param flight Entidad del vuelo que se desea persistir.
     */
    @Override
    public void save(Flight flight) {
        flightRepository.save(flight);
    }
}



