package com.luis.agencia.controller;

import com.luis.agencia.dto.HotelDto;
import com.luis.agencia.service.IHotelService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de hoteles.
 * Aunque IntelliJ indique que 'HotelController' no se usa,
 * en realidad Spring la detecta como @RestController.
 */
@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    /**
     * Constante para indicar resultado exitoso.
     */
    private static final String LITERAL_EXITO = "éxito";
    /**
     * Constante para indicar error interno del servidor.
     */
    private static final String LITERAL_ERROR_INTERNO = "Error interno del servidor";

    /**
     * Servicio para gestionar la lógica de hoteles.
     * Inyectado por Spring.
     */
    @Autowired
    private IHotelService hotelService;

    /**
     * Endpoint para crear un nuevo hotel.
     *
     * @param hotelDto DTO con los datos del hotel a crear.
     * @return Respuesta HTTP con mensaje y código de estado:
     *         <ul>
     *           <li>201 (CREATED): Si el hotel se creó con éxito.</li>
     *           <li>400 (BAD_REQUEST): Si hay datos inválidos (ej. campos vacíos).</li>
     *           <li>409 (CONFLICT): Si el hotel ya existe.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): En caso de error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "El hotel se ha creado con " + LITERAL_EXITO),
            @ApiResponse(responseCode = "400", description = "Datos del hotel inválidos"),
            @ApiResponse(responseCode = "409", description = "El hotel ya existe en la base de datos"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PostMapping("/new")
    public ResponseEntity<String> createHotel(@RequestBody @Valid HotelDto hotelDto) {
        try {
            String result = hotelService.createHotel(hotelDto);
            if (result.contains(LITERAL_EXITO)) {
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else if (result.contains("vacío") || result.contains("campos vacíos")) {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            } else if (result.contains("existe")) {
                return new ResponseEntity<>(result, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            logger.error("Error en createHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno del servidor en createHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO + ".");
        }
    }

    /**
     * Endpoint para editar los datos de un hotel existente.
     *
     * @param id       Identificador del hotel a editar.
     * @param hotelDto DTO con los nuevos datos del hotel.
     * @return Respuesta HTTP con mensaje y código de estado:
     *         <ul>
     *           <li>200 (OK): Si el hotel se editó con éxito.</li>
     *           <li>404 (NOT_FOUND): Si el hotel no fue encontrado.</li>
     *           <li>400 (BAD_REQUEST): Si hay datos inválidos o campos vacíos.</li>
     *           <li>403 (FORBIDDEN): Si el hotel no se puede editar por tener reservas.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): En caso de error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El hotel ha sido editado con " + LITERAL_EXITO),
            @ApiResponse(responseCode = "404", description = "El hotel no ha sido encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "El hotel no puede ser editado porque tiene reservas"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editHotel(@PathVariable Long id, @RequestBody @Valid HotelDto hotelDto) {
        try {
            String result = hotelService.editHotel(id, hotelDto);
            if (result.contains(LITERAL_EXITO)) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else if (result.contains("no existe")) {
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            } else if (result.contains("campos vacíos")) {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            } else if (result.contains("reservas")) {
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            logger.error("Error en editHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad de datos en editHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de integridad de datos: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno del servidor en editHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO + ".");
        }
    }

    /**
     * Endpoint para eliminar (de forma lógica) un hotel.
     *
     * @param id Identificador del hotel a eliminar.
     * @return Respuesta HTTP con mensaje y código de estado:
     *         <ul>
     *           <li>200 (OK): Si el hotel se eliminó correctamente.</li>
     *           <li>404 (NOT_FOUND): Si el hotel no fue encontrado.</li>
     *           <li>403 (FORBIDDEN): Si el hotel no se puede eliminar por tener reservas.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): En caso de error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El hotel ha sido eliminado por lógica"),
            @ApiResponse(responseCode = "404", description = "El hotel no ha sido encontrado"),
            @ApiResponse(responseCode = "403", description = "El hotel no puede ser eliminado porque tiene reservas"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        try {
            String result = hotelService.deleteHotel(id);
            if (result.contains(LITERAL_EXITO)) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else if (result.contains("encontrado")) {
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            } else if (result.contains("reservas")) {
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Error interno del servidor en deleteHotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LITERAL_ERROR_INTERNO + ".");
        }
    }

    /**
     * Endpoint para obtener la información de un hotel por su ID.
     *
     * @param id Identificador del hotel.
     * @return Respuesta HTTP que contiene el DTO del hotel:
     *         <ul>
     *           <li>200 (OK): Si se encontró el hotel.</li>
     *           <li>404 (NOT_FOUND): Si no se encontró el hotel.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel devuelto con " + LITERAL_EXITO),
            @ApiResponse(responseCode = "404", description = "El hotel no ha sido encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        try {
            HotelDto hotelDto = hotelService.findHotelDtoById(id);
            if (hotelDto == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(hotelDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error interno del servidor en getHotelById: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para listar todos los hoteles.
     *
     * @return Respuesta HTTP que contiene la lista de hoteles:
     *         <ul>
     *           <li>200 (OK): Si la lista se devolvió con éxito.</li>
     *           <li>204 (NO_CONTENT): Si no hay hoteles.</li>
     *           <li>500 (INTERNAL_SERVER_ERROR): En caso de error interno.</li>
     *         </ul>
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de hoteles devuelta con " + LITERAL_EXITO),
            @ApiResponse(responseCode = "204", description = "Lista de hoteles vacía"),
            @ApiResponse(responseCode = "500", description = LITERAL_ERROR_INTERNO)
    })
    @GetMapping
    public ResponseEntity<List<HotelDto>> listAllHotels() {
        try {
            List<HotelDto> hotels = hotelService.listHotels();
            if (hotels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error interno del servidor en listAllHotels: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


