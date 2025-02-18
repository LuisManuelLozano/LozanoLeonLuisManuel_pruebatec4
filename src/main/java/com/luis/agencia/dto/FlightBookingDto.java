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
 * DTO que representa la información necesaria para crear o actualizar
 * una reserva de vuelo.
 * Incluye la fecha de la reserva, la cantidad de personas, la lista de pasajeros,
 * y los detalles relacionados con el origen, destino y asientos solicitados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightBookingDto {

    /**
     * Fecha de la reserva.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     * No puede ser nula.
     * Se eliminó la restricción @FutureOrPresent para permitir reservas con vuelos existentes en la B.D.
     */
    @NotNull(message = "La fecha  no puede estar vacía")
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
     * Origen del vuelo.
     * No puede estar nulo ni en blanco.
     */
    @NotNull(message = "El origen no puede estar vacío")
    @NotBlank(message = "El origen no puede estar vacío")
    private String origin;

    /**
     * Destino del vuelo.
     * No puede estar en blanco.
     */
    @NotBlank(message = "El destino no puede estar vacío")
    private String destination;

    /**
     * Cantidad de asientos de turista solicitados en la reserva.
     */
    private int touristSeats;

    /**
     * Cantidad de asientos de clase business solicitados en la reserva.
     */
    private int bussinessSeats;
}



