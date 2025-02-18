package com.luis.agencia.controller;

import com.luis.agencia.dto.RoomDto;
import com.luis.agencia.service.IroomService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de habitaciones.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar habitaciones,
 * además de obtener la disponibilidad de habitaciones según distintos criterios.
 * Aunque IntelliJ indique que 'RoomController' no se usa, Spring la detecta como @RestController.
 */
@RestController
@RequestMapping("/agency/rooms")
@SuppressWarnings({"unused", "SpringJavaInjectionPointsAutowiringInspection"})
public class RoomController {

    /**
     * Servicio para la gestión de habitaciones.
     * Inyectado por Spring.
     */
    @Autowired
    private IroomService roomService;

    /**
     * Constante para el mensaje de error interno del servidor.
     * Ahora la utilizamos en cada método con un bloque try/catch genérico.
     */
    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";

    /**
     * Crea una nueva habitación.
     *
     * @param roomDto Objeto DTO con los datos de la habitación a crear.
     * @return ResponseEntity con mensaje resultante y código de estado:
     *         <ul>
     *           <li>201 (CREATED): Si la habitación se crea con éxito.</li>
     *           <li>400 (BAD_REQUEST): Si los datos son inválidos o el hotel no existe.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or hotel does not exist"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> createRoom(@RequestBody RoomDto roomDto) {
        try {
            String result = roomService.createRoom(roomDto);
            HttpStatus status = HttpStatus.CREATED;
            if (result.equals("El ID del hotel es obligatorio.")
                    || result.equals("El hotel no existe.")
                    || result.equals("La habitación ya existe en este hotel para las fechas proporcionadas.")) {
                status = HttpStatus.BAD_REQUEST;
            }
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            // Captura genérica para devolver un 500 en caso de error interno
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene las habitaciones disponibles para un hotel en un rango de fechas.
     *
     * @param hotelId  Identificador del hotel.
     * @param fromDate Fecha de inicio de la disponibilidad (formato "dd-MM-yyyy").
     * @param toDate   Fecha de fin de la disponibilidad (formato "dd-MM-yyyy").
     * @return ResponseEntity con la lista de habitaciones disponibles:
     *         <ul>
     *           <li>200 (OK): Si se encuentran habitaciones disponibles.</li>
     *           <li>204 (NO_CONTENT): Si no hay habitaciones disponibles.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @GetMapping("/availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available rooms"),
            @ApiResponse(responseCode = "204", description = "No available rooms found"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> getAvailableRooms(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate toDate) {
        try {
            List<RoomDto> availableRooms = roomService.findAvailableRooms(hotelId, fromDate, toDate);
            if (availableRooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(availableRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista las habitaciones asociadas a un hotel específico.
     *
     * @param hotelId Identificador del hotel.
     * @return ResponseEntity con la lista de habitaciones en formato DTO:
     *         <ul>
     *           <li>200 (OK): Si se encuentran habitaciones para el hotel.</li>
     *           <li>204 (NO_CONTENT): Si no se encuentran habitaciones para el hotel.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @GetMapping("/hotel/{hotelId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms"),
            @ApiResponse(responseCode = "204", description = "No rooms found for the hotel"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> listRoomsByHotel(@PathVariable Long hotelId) {
        try {
            List<RoomDto> rooms = roomService.listRoomsByHotel(hotelId);
            if (rooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza los datos de una habitación.
     *
     * @param roomId  Identificador de la habitación a actualizar.
     * @param roomDto DTO con los nuevos datos de la habitación.
     * @return ResponseEntity con mensaje resultante y código de estado:
     *         <ul>
     *           <li>200 (OK): Si se actualiza la habitación con éxito.</li>
     *           <li>404 (NOT_FOUND): Si la habitación no se encuentra.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @PutMapping("/edit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room updated successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> updateRoom(
            @RequestParam("roomId") Long roomId,
            @RequestBody RoomDto roomDto) {
        try {
            String result = roomService.updateRoom(roomId, roomDto);
            HttpStatus status = result.equals("Room updated successfully.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina una habitación.
     *
     * @param roomId Identificador de la habitación a eliminar.
     * @return ResponseEntity con mensaje resultante y código de estado:
     *         <ul>
     *           <li>200 (OK): Si se elimina la habitación con éxito.</li>
     *           <li>404 (NOT_FOUND): Si la habitación no se encuentra.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @DeleteMapping("/delete/{roomId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> deleteRoom(@PathVariable Long roomId) {
        try {
            String result = roomService.deleteRoom(roomId);
            HttpStatus status = result.equals("Room deleted successfully.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene los datos de una habitación por su identificador.
     *
     * @param roomId Identificador de la habitación.
     * @return ResponseEntity con el DTO de la habitación:
     *         <ul>
     *           <li>200 (OK): Si se encuentra la habitación.</li>
     *           <li>404 (NOT_FOUND): Si la habitación no existe.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @GetMapping("/{roomId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved room"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> getRoomById(@PathVariable Long roomId) {
        try {
            RoomDto roomDto = roomService.findRoomById(roomId);
            if (roomDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La habitación no existe.");
            }
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todas las habitaciones.
     *
     * @return ResponseEntity con la lista completa de habitaciones en formato DTO:
     *         <ul>
     *           <li>200 (OK): Si se obtienen las habitaciones.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all rooms"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> listAllRooms() {
        try {
            List<RoomDto> rooms = roomService.listAllRooms();
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene las habitaciones disponibles por destino en un rango de fechas.
     *
     * @param destination Destino (lugar) del hotel.
     * @param fromDate    Fecha de inicio de la disponibilidad (formato "dd-MM-yyyy").
     * @param toDate      Fecha de fin de la disponibilidad (formato "dd-MM-yyyy").
     * @return ResponseEntity con la lista de habitaciones disponibles en formato DTO:
     *         <ul>
     *           <li>200 (OK): Si se encuentran habitaciones disponibles.</li>
     *           <li>204 (NO_CONTENT): Si no se encuentran habitaciones disponibles.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): Si ocurre un error interno.</li>
     *         </ul>
     */
    @GetMapping("/availability-by-destination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available rooms by destination"),
            @ApiResponse(responseCode = "204", description = "No available rooms found for the destination"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Object> getAvailableRoomsByDestination(
            @RequestParam("destination") String destination,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate toDate) {
        try {
            List<RoomDto> availableRooms = roomService.findAvailableRoomsByDestination(destination, fromDate, toDate);
            if (availableRooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(availableRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




