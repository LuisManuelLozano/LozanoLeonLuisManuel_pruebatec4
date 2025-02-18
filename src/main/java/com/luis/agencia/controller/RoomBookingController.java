package com.luis.agencia.controller;

import com.luis.agencia.dto.RoomBookingDto;
import com.luis.agencia.dto.RoomBookingDetailDto;
import com.luis.agencia.dto.RoomDto;
import com.luis.agencia.service.IRoomBookingService;
import com.luis.agencia.service.IroomService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la gestión de reservas de habitaciones.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar reservas,
 * así como para obtener la disponibilidad de habitaciones según tipo y destino.
 * Aunque IntelliJ indique que 'RoomBookingController' no se usa,
 * en realidad Spring la detecta como @RestController.
 */
@RestController
@RequestMapping("agency/room-booking")
public class RoomBookingController {

    /**
     * Servicio para la gestión de reservas de habitaciones.
     */
    @Autowired
    private IRoomBookingService roomBookingService;

    /**
     * Servicio para la gestión de habitaciones.
     */
    @Autowired
    private IroomService roomservice;

    /**
     * Constante para unificar el mensaje de error interno del servidor.
     */
    private static final String LITERAL_ERROR_INTERNO = "Error interno del servidor";

    /**
     * Maneja y devuelve los errores de validación en una respuesta HTTP.
     *
     * @param bindingResult Resultado de la validación del DTO.
     * @return ResponseEntity con código 400 (BAD_REQUEST) y la cadena de errores.
     */
    private ResponseEntity<Object> handleValidationErrors(BindingResult bindingResult) {
        String errors = bindingResult.getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Crea una nueva reserva de habitación.
     *
     * @param roomBookingDto DTO con la información de la reserva (fechas, habitaciones, pasajeros, etc.).
     * @param bindingResult  Resultado de la validación del DTO.
     * @return ResponseEntity con el DTO de la reserva creada y código 201 (CREATED), o mensaje de error.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PostMapping("/new")
    public ResponseEntity<Object> createRoomBooking(
            @Valid @RequestBody RoomBookingDto roomBookingDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        try {
            RoomBookingDto created = roomBookingService.createRoomBooking(roomBookingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Actualiza una reserva de habitación existente.
     *
     * @param id             Identificador de la reserva a actualizar.
     * @param roomBookingDto DTO con la nueva información de la reserva.
     * @param bindingResult  Resultado de la validación del DTO.
     * @return ResponseEntity con el DTO actualizado y código 200 (OK), o mensaje de error.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updateRoomBooking(
            @PathVariable Long id,
            @Valid @RequestBody RoomBookingDto roomBookingDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        try {
            RoomBookingDto updated = roomBookingService.updateRoomBooking(id, roomBookingDto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg.contains("no existe")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Elimina una reserva de habitación.
     *
     * @param id Identificador de la reserva a eliminar.
     * @return ResponseEntity con mensaje de resultado y código 200 (OK) si se elimina,
     *         o mensaje de error según corresponda.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteRoomBooking(@PathVariable Long id) {
        try {
            roomBookingService.deleteRoomBooking(id);
            return ResponseEntity.ok("Reserva eliminada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Lista todas las reservas de habitación con sus detalles.
     *
     * @return ResponseEntity con la lista de reservas en formato DTO y código 200 (OK),
     *         204 (NO_CONTENT) si no hay reservas, o 500 (INTERNAL_SERVER_ERROR) en caso de error.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reservas devuelto con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay reservas"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping
    public ResponseEntity<List<RoomBookingDetailDto>> listAllRoomBookings() {
        try {
            List<RoomBookingDetailDto> bookings = roomBookingService.listAllRoomBookings();
            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene los detalles de una reserva de habitación por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return ResponseEntity con el DTO de la reserva y código 200 (OK) si se encuentra,
     *         o mensaje de error con código 404 (NOT_FOUND) o 500 (INTERNAL_SERVER_ERROR) según corresponda.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva devuelta con éxito"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoomBookingDetailById(@PathVariable Long id) {
        try {
            RoomBookingDetailDto detailDto = roomBookingService.getRoomBookingDetailById(id);
            if (detailDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
            }
            return ResponseEntity.ok(detailDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Obtiene la lista de habitaciones disponibles filtradas por tipo y destino,
     * en un rango de fechas especificado.
     *
     * @param roomType    Tipo de habitación (ej. SINGLE, DOUBLE).
     * @param destination Destino (lugar) del hotel.
     * @param fromDate    Fecha de inicio de la disponibilidad (formato "dd-MM-yyyy").
     * @param toDate      Fecha de fin de la disponibilidad (formato "dd-MM-yyyy").
     * @return ResponseEntity con la lista de habitaciones disponibles en formato DTO y código 200 (OK),
     *         o 204 (NO_CONTENT) si no se encuentran, o 500 (INTERNAL_SERVER_ERROR) en caso de error.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitaciones disponibles obtenidas"),
            @ApiResponse(responseCode = "204", description = "No se han encontrado habitaciones disponibles"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping("/availability-by-type-destination")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByTypeAndDestination(
            @RequestParam("roomType") String roomType,
            @RequestParam("destination") String destination,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate toDate) {

        List<RoomDto> availableRooms = roomservice
                .findAvailableRoomsByTypeAndDestination(roomType, fromDate, toDate, destination);

        if (availableRooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }
}



