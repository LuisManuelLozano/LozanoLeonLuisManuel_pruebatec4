package com.luis.agencia.controller;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.model.Flight;
import com.luis.agencia.service.FlightService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar los vuelos ({@link Flight}).
 * Proporciona endpoints para crear, editar, eliminar y consultar vuelos,
 * incluyendo la búsqueda de vuelos disponibles por fechas y destino.
 * Aunque IntelliJ indique que "FlightController" no se usa,
 * en realidad Spring la detecta como @RestController.
 */
@RestController
@RequestMapping("agency/flights")
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    /**
     * Constante para evitar duplicar el literal de error interno del servidor.
     */
    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor.";

    /**
     * Servicio para gestionar la lógica de los vuelos.
     * Inyectado por Spring.
     */
    @Autowired
    private FlightService flightService;

    /**
     * Crea un nuevo vuelo a partir de los datos proporcionados en el DTO.
     *
     * @param flightDto DTO con la información del vuelo a crear.
     * @return Respuesta HTTP que indica el resultado de la operación:
     *         <ul>
     *             <li>201 (CREATED) si el vuelo se crea con éxito.</li>
     *             <li>400 (BAD_REQUEST) si hay datos inválidos.</li>
     *             <li>500 (INTERNAL_SERVER_ERROR) si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "El vuelo se ha creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del vuelo inválidos"),
            @ApiResponse(responseCode = "409", description = "El vuelo ya existe en la base de datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/new")
    public ResponseEntity<Object> createFlight(@RequestBody @Valid FlightDto flightDto) {
        try {
            flightService.createFlight(flightDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Error en createFlight: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno del servidor en createFlight: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Edita los datos de un vuelo existente, identificado por su ID.
     * Requiere un rol de FLIGHT_ADMIN para poder realizar la operación.
     *
     * @param id          Identificador del vuelo que se desea editar.
     * @param flightDto   DTO con los nuevos datos del vuelo.
     * @param bindingResult Contiene los errores de validación, en caso de haberlos.
     * @return Respuesta HTTP que indica el resultado de la operación:
     *         <ul>
     *             <li>200 (OK) si el vuelo se edita con éxito.</li>
     *             <li>404 (NOT_FOUND) si el vuelo no existe.</li>
     *             <li>400 (BAD_REQUEST) si hay errores de validación o campos obligatorios vacíos.</li>
     *             <li>409 (CONFLICT) si ya existe otro vuelo con el mismo número.</li>
     *             <li>403 (FORBIDDEN) si el vuelo no está activo y no se puede editar.</li>
     *             <li>500 (INTERNAL_SERVER_ERROR) si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El vuelo ha sido editado con éxito"),
            @ApiResponse(responseCode = "409", description = "El número de vuelo ya existe en la base de datos"),
            @ApiResponse(responseCode = "404", description = "El vuelo no ha sido encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "El vuelo no puede ser eliminado porque no está activo"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('FLIGHT_ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> editFlight(@PathVariable Long id,
                                             @RequestBody @Valid FlightDto flightDto,
                                             BindingResult bindingResult) {
        logger.debug("editFlight llamado con ID: {}", id);

        // Verificar si el vuelo existe
        FlightDto existingFlight = flightService.findFlightDtoById(id);
        if (existingFlight == null) {
            logger.debug("Vuelo no encontrado con ID: {}", id);
            return new ResponseEntity<>("El vuelo a editar no ha sido encontrado", HttpStatus.NOT_FOUND);
        }

        // Verificar errores de validación
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            logger.debug("Errores de validación: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }

        // Validar campos obligatorios
        if (flightDto.getFlightNumber().isEmpty() || flightDto.getOrigin().isEmpty() || flightDto.getDestination().isEmpty()) {
            logger.debug("Campos obligatorios vacíos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los campos obligatorios no pueden estar vacíos");
        }

        try {
            logger.debug("Intentando editar vuelo con ID: {}", id);
            String resultado = flightService.editFlight(id, flightDto);
            logger.debug("Resultado de editar vuelo: {}", resultado);

            if (resultado.contains("no ha sido encontrado")) {
                return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
            } else if (resultado.contains("éxito")) {
                return new ResponseEntity<>(resultado, HttpStatus.OK);
            } else if (resultado.contains("ya existe")) {
                return new ResponseEntity<>(resultado, HttpStatus.CONFLICT);
            } else if (resultado.contains("activo")) {
                logger.debug("El vuelo no está activo: {}", id);
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            logger.debug("Error de IllegalArgumentException: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad de datos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de integridad de datos: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno del servidor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los vuelos disponibles en el sistema, retornándolos en formato DTO.
     *
     * @return Respuesta HTTP con la lista de vuelos y el código de estado:
     *         <ul>
     *             <li>200 (OK) si la lista no está vacía.</li>
     *             <li>204 (NO_CONTENT) si no hay vuelos en la base de datos.</li>
     *             <li>500 (INTERNAL_SERVER_ERROR) si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos devuelta con éxito"),
            @ApiResponse(responseCode = "204", description = "lista de vuelos vacía"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<FlightDto>> listFlights() {
        try {
            List<FlightDto> listaVuelos = flightService.listFlights();
            if (listaVuelos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(listaVuelos, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error interno del servidor en listFlights: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca un vuelo por su identificador y lo devuelve en formato DTO.
     *
     * @param id Identificador del vuelo a buscar.
     * @return Respuesta HTTP con el vuelo en caso de existir:
     *         <ul>
     *             <li>200 (OK) si se encuentra el vuelo.</li>
     *             <li>404 (NOT_FOUND) si no existe un vuelo con ese ID.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo devuelto con éxito"),
            @ApiResponse(responseCode = "404", description = "El vuelo no existe con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> flightById(@PathVariable Long id) {
        FlightDto flightDto = flightService.findFlightDtoById(id);
        if (flightDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flightDto, HttpStatus.OK);
    }

    /**
     * Elimina un vuelo de forma lógica (lo marca como inactivo) siempre que no tenga reservas.
     *
     * @param id Identificador del vuelo a eliminar.
     * @return Respuesta HTTP con el resultado de la operación:
     *         <ul>
     *             <li>200 (OK) si el vuelo se elimina lógicamente.</li>
     *             <li>404 (NOT_FOUND) si no existe el vuelo.</li>
     *             <li>403 (FORBIDDEN) si el vuelo no se puede eliminar por tener reservas pendientes.</li>
     *             <li>500 (INTERNAL_SERVER_ERROR) si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El vuelo ha sido eliminado por lógica"),
            @ApiResponse(responseCode = "404", description = "El vuelo no existe con ese id"),
            @ApiResponse(responseCode = "403", description = "No se puede eliminar un vuelo que tiene reservas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteFlight(@PathVariable Long id) {
        String resultado = flightService.deleteFlight(id);
        if (resultado.contains("lógica")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else if (resultado.contains("pendientes")) {
            return new ResponseEntity<>(resultado, HttpStatus.FORBIDDEN);
        } else if (resultado.contains("existe")) {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Busca vuelos disponibles en base a fechas de salida y llegada, origen y destino.
     *
     * @param dateFrom    Fecha de salida, con el patrón "dd/MM/yyyy".
     * @param dateTo      Fecha de llegada, con el patrón "dd/MM/yyyy".
     * @param origin      Origen del vuelo.
     * @param destination Destino del vuelo.
     * @return Respuesta HTTP que contiene la lista de vuelos disponibles o un mensaje de error:
     *         <ul>
     *             <li>200 (OK) si se encuentran vuelos disponibles.</li>
     *             <li>204 (NO_CONTENT) si no hay vuelos disponibles.</li>
     *             <li>400 (BAD_REQUEST) si la solicitud es incorrecta (no se maneja en este método, pero podría surgir).</li>
     *             <li>500 (INTERNAL_SERVER_ERROR) si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos devuelta con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay vuelos disponibles"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search")
    public ResponseEntity<Object> getVuelosDisponibles(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {

        try {
            List<Flight> availableFlights = flightService.getVuelosDisponibles(dateFrom, dateTo, origin, destination);
            if (availableFlights.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(availableFlights, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error interno del servidor en getVuelosDisponibles: {}", e.getMessage());
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






