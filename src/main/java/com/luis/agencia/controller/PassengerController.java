package com.luis.agencia.controller;

import com.luis.agencia.dto.PassengerDto;
import com.luis.agencia.service.IPassengerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la gestión de pasajeros.
 * Aunque IntelliJ indique que 'PassengerController' no se usa,
 * en realidad Spring la detecta como @RestController.
 */
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    /**
     * Servicio para gestionar la lógica de pasajeros.
     * Inyectado por Spring.
     */
    @Autowired
    private IPassengerService passengerService;

    /**
     * Constante para el mensaje de error interno del servidor.
     */
    private static final String LITERAL_ERROR_INTERNO = "Error interno del servidor";

    /**
     * Endpoint para obtener el listado de todos los pasajeros.
     *
     * @return Respuesta HTTP con la lista de pasajeros en formato DTO:
     *         <ul>
     *           <li>200 (OK): Si se obtienen pasajeros.</li>
     *           <li>204 (NO_CONTENT): Si no hay pasajeros disponibles.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de pasajeros obtenido con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay pasajeros disponibles"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping
    public ResponseEntity<List<PassengerDto>> listPassengers() {
        try {
            List<PassengerDto> passengers = passengerService.listPassengers();
            if (passengers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(passengers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para obtener un pasajero por su identificador.
     *
     * @param id Identificador del pasajero.
     * @return Respuesta HTTP con el DTO del pasajero:
     *         <ul>
     *           <li>200 (OK): Si se encuentra el pasajero.</li>
     *           <li>404 (NOT_FOUND): Si el pasajero no existe.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassengerDto> getPassengerById(@PathVariable Long id) {
        try {
            PassengerDto passengerDto = passengerService.findPassengerDtoById(id);
            if (passengerDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(passengerDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para crear un nuevo pasajero.
     *
     * @param passengerDto   DTO con los datos del pasajero a crear.
     * @param bindingResult  Contiene los errores de validación, si existen.
     * @return Respuesta HTTP con el DTO del pasajero creado o con la lista de errores:
     *         <ul>
     *           <li>201 (CREATED): Si se crea el pasajero con éxito.</li>
     *           <li>400 (BAD_REQUEST): Si hay datos inválidos.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pasajero creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del pasajero inválidos"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PostMapping("/new")
    public ResponseEntity<Object> createPassenger(
            @Valid @RequestBody PassengerDto passengerDto,
            BindingResult bindingResult) {

        // Si hay errores de validación, se devuelven como BAD_REQUEST.
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            PassengerDto created = passengerService.createPassenger(passengerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            // Errores controlados por la lógica de servicio.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // En caso de error interno.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Endpoint para actualizar los datos de un pasajero existente.
     *
     * @param id             Identificador del pasajero a actualizar.
     * @param passengerDto   DTO con los nuevos datos del pasajero.
     * @param bindingResult  Contiene los errores de validación, si existen.
     * @return Respuesta HTTP con el DTO actualizado o con la lista de errores:
     *         <ul>
     *           <li>200 (OK): Si se actualiza el pasajero con éxito.</li>
     *           <li>404 (NOT_FOUND): Si el pasajero no existe.</li>
     *           <li>400 (BAD_REQUEST): Si hay datos inválidos.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del pasajero inválidos"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody PassengerDto passengerDto,
            BindingResult bindingResult) {

        // Validar el DTO y capturar errores.
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            PassengerDto updated = passengerService.updatePassenger(id, passengerDto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            // Si el mensaje contiene "no existe", se considera 404.
            if (e.getMessage().contains("no existe")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }

    /**
     * Endpoint para eliminar un pasajero de la base de datos.
     *
     * @param id Identificador del pasajero a eliminar.
     * @return Respuesta HTTP con mensaje de resultado:
     *         <ul>
     *           <li>200 (OK): Si se elimina el pasajero con éxito.</li>
     *           <li>404 (NOT_FOUND): Si el pasajero no existe.</li>
     *           <li>400 (BAD_REQUEST): Si ocurre otro error controlado.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pasajero eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deletePassenger(@PathVariable Long id) {
        try {
            passengerService.deletePassenger(id);
            return ResponseEntity.ok("Pasajero eliminado con éxito");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no existe")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO);
        }
    }
}




