package com.luis.agencia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO que representa el detalle de una reserva de vuelo.
 * Incluye información como el identificador del vuelo, la fecha de la reserva,
 * la cantidad de personas, el costo total, y otros datos relacionados con el vuelo.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingDetailDto {

    /**
     * Identificador único de la reserva.
     */
    private Long id;

    /**
     * Identificador del vuelo asociado a la reserva.
     * No puede ser nulo y se requiere que sea mayor que 0.
     */
    @NotNull(message = "El id de vuelo puede estar vacío")
    @Size(min = 1, message = "Debe ser mayor que 0")
    private Long flightId;

    /**
     * Fecha de la reserva.
     * Se formatea como "dd-MM-yyyy" en formato JSON.
     * No puede ser nula. Se eliminó la restricción @FutureOrPresent
     * para permitir reservas con fechas existentes en la B.D.
     */
    @NotNull(message = "La fecha no puede estar vacía")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    /**
     * Cantidad de personas para la reserva.
     * Debe ser al menos 1.
     */
    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    private int peopleQ;

    /**
     * Lista de identificadores de pasajeros asociados a la reserva.
     * No puede estar vacía y debe contener al menos un pasajero.
     */
    @NotNull(message = "Los pasajeros no pueden estar vacíos")
    @Size(min = 1, message = "Debe haber al menos un pasajero")
    private List<Long> passengersIds;

    /**
     * Costo total de la reserva.
     * Debe ser un número positivo.
     */
    @Positive(message = "El costo total debe ser un número positivo")
    private double totalCost;

    /**
     * Origen del vuelo.
     * No puede estar en blanco.
     */
    @NotBlank(message = "El origen no puede estar vacío")
    private String origin;

    /**
     * Destino del vuelo.
     * No puede estar en blanco.
     */
    @NotBlank(message = "El destino no puede estar vacío")
    private String destination;

    /**
     * Número del vuelo.
     * No puede estar en blanco.
     */
    @NotBlank(message = "El número de vuelo no puede estar vacío")
    private String flightNumber;

    /**
     * Nombre del vuelo.
     * No puede estar en blanco.
     */
    @NotBlank(message = "El nombre del vuelo no puede estar vacío")
    private String flightName;

    /**
     * Cantidad de asientos de turista solicitados en la reserva.
     */
    private int touristSeats;

    /**
     * Cantidad de asientos de clase business solicitados en la reserva.
     */
    private int bussinessSeats;

    /**
     * Se ha decidido eliminar el campo 'passengers' para que no se devuelva en la respuesta.
     */
}


