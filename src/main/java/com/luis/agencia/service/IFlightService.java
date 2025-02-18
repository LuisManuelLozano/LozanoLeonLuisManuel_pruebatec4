package com.luis.agencia.service;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.model.Flight;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con vuelos.
 * Proporciona métodos para crear, editar, eliminar y consultar vuelos, así como para obtener vuelos disponibles.
 */
public interface IFlightService {

     /**
      * Busca un vuelo por su identificador.
      *
      * @param id Identificador del vuelo.
      * @return La entidad {@link Flight} encontrada, o {@code null} si no se encuentra.
      */
     Flight findFlightById(Long id);

     /**
      * Crea un nuevo vuelo en la base de datos.
      *
      * @param flightDto DTO que contiene la información del vuelo a crear.
      */
     void createFlight(FlightDto flightDto);

     /**
      * Retorna una lista de vuelos en formato DTO.
      *
      * @return Lista de {@link FlightDto} con la información de los vuelos.
      */
     List<FlightDto> listFlights();

     /**
      * Edita la información de un vuelo existente.
      *
      * @param id         Identificador del vuelo a editar.
      * @param flightDto  DTO que contiene la información actualizada del vuelo.
      * @return Un mensaje indicando el resultado de la operación (por ejemplo, éxito o error).
      */
     String editFlight(Long id, FlightDto flightDto);

     /**
      * Busca un vuelo por su identificador y lo retorna en formato DTO.
      *
      * @param id Identificador del vuelo.
      * @return El DTO {@link FlightDto} del vuelo encontrado, o {@code null} si no se encuentra.
      */
     FlightDto findFlightDtoById(Long id);

     /**
      * Elimina un vuelo de la base de datos de forma lógica o física, según la implementación.
      *
      * @param id Identificador del vuelo a eliminar.
      * @return Un mensaje indicando el resultado de la eliminación.
      */
     String deleteFlight(Long id);

     /**
      * Obtiene una lista de vuelos disponibles en base a la fecha de salida, fecha de llegada,
      * origen y destino.
      *
      * @param dateFrom  Fecha de salida.
      * @param dateTo    Fecha de llegada.
      * @param origin    Origen del vuelo.
      * @param destination Destino del vuelo.
      * @return Lista de {@link Flight} que cumplen con los criterios.
      */
     List<Flight> getVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);

     /**
      * Obtiene una lista de vuelos cuyo campo de fecha (ida o vuelta) coincide con la fecha proporcionada.
      *
      * @param local Fecha a buscar en los campos de fecha de salida o llegada.
      * @return Lista de {@link Flight} que coinciden con la fecha proporcionada.
      */
     List<Flight> findByDateFromOrByDateTo(LocalDate local);

     /**
      * Persiste o actualiza un vuelo en la base de datos.
      *
      * @param flight La entidad {@link Flight} que se desea guardar.
      */
     void save(Flight flight);
}

