package com.luis.agencia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO que representa los datos de un vuelo.
 * Contiene información sobre el vuelo, incluyendo su nombre, número, origen, destino,
 * cantidad y precio de asientos, fechas de ida y vuelta, y estado de actividad.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {

    /**
     * Nombre del vuelo.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El nombre del vuelo no puede estar vacío")
    private String name;

    /**
     * Número de vuelo.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El número de vuelo no puede estar vacío")
    private String flightNumber;

    /**
     * Origen del vuelo.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El origen no puede estar vacío")
    private String origin;

    /**
     * Destino del vuelo.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El destino no puede estar vacío")
    private String destination;

    /**
     * Cantidad de asientos de clase business disponibles.
     * Debe ser un número positivo.
     */
    @Positive(message = "El número de asientos tiene que ser positivo")
    private int businessSeatsQ;

    /**
     * Cantidad de asientos de clase economy disponibles.
     * Debe ser un número positivo.
     */
    @Positive(message = "El número de asientos tiene que ser positivo")
    private int economySeatsQ;

    /**
     * Precio por asiento de clase business.
     * Debe ser un número positivo.
     */
    @Positive(message = "El precio del asiento tiene que ser positivo")
    private double businessSeatPrice;

    /**
     * Precio por asiento de clase economy.
     * Debe ser un número positivo.
     */
    @Positive(message = "El precio del asiento tiene que ser positivo")
    private double economySeatPrice;

    /**
     * Fecha de ida del vuelo.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     * No puede estar vacía.
     */
    @NotNull(message = "La fecha de ida no puede estar vacía")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateFrom;

    /**
     * Fecha de vuelta del vuelo.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     * No puede estar vacía.
     */
    @NotNull(message = "La fecha de vuelta no puede estar vacía")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;

    /**
     * Indicador del estado del vuelo.
     * 'true' si el vuelo está activo, 'false' en caso contrario.
     */
    private boolean activa;
}






