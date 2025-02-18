package com.luis.agencia.controller;

import com.luis.agencia.dto.FlightBookingDetailDto;
import com.luis.agencia.dto.FlightBookingDto;
import com.luis.agencia.model.FlightBooking;
import com.luis.agencia.service.IFlightBookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestionar las reservas de vuelo ({@link FlightBooking}).
 * Proporciona endpoints para crear, editar, eliminar y obtener reservas.
 * Aunque IntelliJ pueda advertir que la clase 'no se usa', Spring la detecta
 * y la expone como un RestController.
 */
@RestController
@RequestMapping("/agency/flight-booking")
public class FlightBookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightBookingController.class);

    /**
     * Constante para evitar la duplicación del literal "Internal server error".
     */
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    /**
     * Servicio que gestiona la lógica de las reservas de vuelo.
     * Inyectado por Spring.
     */
    @Autowired
    private IFlightBookingService flightBookingService;

    /**
     * Crea una nueva reserva de vuelo.
     *
     * @param dto           DTO con la información de la reserva.
     * @param bindingResult Objeto que contiene los errores de validación.
     * @return Respuesta con el detalle de la reserva creada o un mensaje de error.
     */
    @PostMapping("/new")
    public ResponseEntity<Object> createFlightBooking(@Valid @RequestBody FlightBookingDto dto,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation errors: " + bindingResult.getFieldErrors());
        }
        try {
            FlightBookingDetailDto detailDto = flightBookingService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(detailDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Exception in createFlightBooking", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Edita una reserva de vuelo existente.
     *
     * @param id            Identificador de la reserva.
     * @param dto           DTO con la nueva información de la reserva.
     * @param bindingResult Objeto que contiene los errores de validación.
     * @return Respuesta con el detalle de la reserva editada o un mensaje de error.
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> editFlightBooking(@PathVariable Long id,
                                                    @Valid @RequestBody FlightBookingDto dto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation errors: " + bindingResult.getFieldErrors());
        }
        try {
            FlightBookingDetailDto detailDto = flightBookingService.edit(id, dto);
            return ResponseEntity.ok(detailDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Exception in editFlightBooking", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina una reserva de vuelo existente.
     *
     * @param id Identificador de la reserva a eliminar.
     * @return Respuesta con un mensaje de éxito o error.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteFlightBooking(@PathVariable Long id) {
        try {
            flightBookingService.delete(id);
            return ResponseEntity.ok("Flight booking deleted successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Exception in deleteFlightBooking", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la lista de todas las reservas de vuelo.
     *
     * @return Lista de {@link FlightBooking} o mensaje de error.
     */
    @GetMapping
    public ResponseEntity<Object> getAllFlightBookings() {
        try {
            List<FlightBooking> bookings = flightBookingService.findAll();
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Exception in getAllFlightBookings", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene los detalles de una reserva de vuelo por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return Entidad {@link FlightBooking} o mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFlightBookingById(@PathVariable Long id) {
        try {
            FlightBooking booking = flightBookingService.findById(id);
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Exception in getFlightBookingById", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR);
        }
    }
}






