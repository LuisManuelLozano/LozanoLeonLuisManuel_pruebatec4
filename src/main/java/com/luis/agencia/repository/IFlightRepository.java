package com.luis.agencia.repository;
import com.luis.agencia.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad {@link Flight}.
 * Define operaciones de acceso a datos, además de consultas personalizadas para obtener vuelos
 * en función de diferentes parámetros como número de vuelo, fechas y rutas.
 */
public interface IFlightRepository extends JpaRepository<Flight, Long> {

    /**
     * Busca un vuelo por su número único.
     *
     * @param flightNumber el número del vuelo.
     * @return un Optional que contiene el vuelo encontrado o vacío si no existe.
     */
    Optional<Flight> findByFlightNumber(String flightNumber);

    /**
     * Obtiene una lista de vuelos que coinciden con la fecha de salida, origen y destino especificados.
     *
     * @param dateFrom la fecha de salida del vuelo.
     * @param origin   el origen del vuelo.
     * @param destination el destino del vuelo.
     * @return lista de vuelos que cumplen con los criterios.
     */
    List<Flight> findByDateFromAndOriginAndDestination(LocalDate dateFrom, String origin, String destination);

    /**
     * Obtiene una lista de vuelos que coinciden con la fecha de llegada, origen y destino especificados.
     *
     * @param dateTo   la fecha de llegada del vuelo.
     * @param origin   el origen del vuelo.
     * @param destination el destino del vuelo.
     * @return lista de vuelos que cumplen con los criterios.
     */
    List<Flight> findByDateToAndOriginAndDestination(LocalDate dateTo, String origin, String destination);

    /**
     * Consulta personalizada que obtiene vuelos donde la fecha de salida o la fecha de llegada
     * coincide con la fecha proporcionada.
     *
     * @param date la fecha a comparar tanto para la salida como para la llegada del vuelo.
     * @return lista de vuelos que tienen la fecha indicada en la fecha de salida o llegada.
     */
    @Query("SELECT f FROM Flight f WHERE f.dateFrom = :date OR f.dateTo = :date")
    List<Flight> findByDateFromOrDateTo(@Param("date") LocalDate date);
}


