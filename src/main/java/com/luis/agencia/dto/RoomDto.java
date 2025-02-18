package com.luis.agencia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luis.agencia.model.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO que representa la información de una habitación.
 * Incluye el identificador de la habitación, el tipo de habitación, el identificador del hotel,
 * y las fechas de disponibilidad.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    /**
     * Identificador único de la habitación.
     */
    private Long id;

    /**
     * Tipo de la habitación (por ejemplo, SINGLE o DOUBLE).
     * Este campo no puede ser nulo.
     */
    @NotNull(message = "El tipo de habitación no puede estar vacío")
    private RoomType roomType;

    /**
     * Identificador del hotel al que pertenece la habitación.
     * Este campo no puede ser nulo.
     */
    @NotNull(message = "El ID del hotel no puede estar vacía")
    private Long hotelId;

    /**
     * Fecha de inicio de la disponibilidad de la habitación.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha de disponibilidad desde no puede estar vacía")
    private LocalDate disponibilityDateFrom;

    /**
     * Fecha de fin de la disponibilidad de la habitación.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha de disponibilidad hasta no puede estar vacía")
    private LocalDate disponibilityDateTo;
}


