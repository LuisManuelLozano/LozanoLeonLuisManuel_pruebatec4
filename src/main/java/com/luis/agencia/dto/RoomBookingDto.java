package com.luis.agencia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información necesaria para crear o actualizar
 * una reserva de habitación.
 * Incluye datos como las fechas de inicio y fin, número de noches, cantidad de personas,
 * huéspedes asociados, cantidad de habitaciones (dobles y simples), destino, costo total
 * y el nombre del hotel.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RoomBookingDto {

    /**
     * Identificador único de la reserva (opcional para actualizaciones).
     */
    private Long id;

    /**
     * Fecha de inicio de la reserva.
     * Debe ser en el presente o en el futuro.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio debe ser en el presente o futuro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateFrom;

    /**
     * Fecha de fin de la reserva.
     * Debe ser en el presente o en el futuro.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @NotNull(message = "La fecha de fin no puede estar vacía")
    @FutureOrPresent(message = "La fecha de fin debe ser en el presente o futuro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;

    /**
     * Número de noches que dura la reserva.
     * Debe ser al menos 1.
     */
    @Min(value = 1, message = "El número de noches debe ser al menos 1")
    private int nights;

    /**
     * Cantidad de personas para las que se realiza la reserva.
     * Debe ser un número positivo.
     */
    @Positive(message = "La cantidad de personas debe ser un número positivo")
    private int peopleQ;

    /**
     * Lista de identificadores de huéspedes (pasajeros) asociados a la reserva.
     * No puede estar vacía y debe contener al menos un elemento.
     */
    @NotNull(message = "Los huéspedes no pueden estar vacíos")
    @Size(min = 1, message = "Debe haber al menos un huésped")
    private List<Long> passengersIds;

    /**
     * Cantidad de habitaciones dobles solicitadas en la reserva.
     * No puede ser negativa.
     */
    @Min(value = 0, message = "La cantidad de habitaciones dobles no puede ser negativa")
    private int doubleRoomQ;

    /**
     * Cantidad de habitaciones simples solicitadas en la reserva.
     * No puede ser negativa.
     */
    @Min(value = 0, message = "La cantidad de habitaciones simples no puede ser negativa")
    private int singleRoomQ;

    /**
     * Destino de la reserva (lugar del hotel).
     * No puede estar en blanco.
     */
    @NotBlank(message = "El destino no puede estar vacío")
    private String destination;

    /**
     * Costo total de la reserva, calculado en tiempo de ejecución.
     * Este campo no se persistirá.
     */
    private double totalCost;

    /**
     * Nombre del hotel asociado a la reserva.
     */
    private String hotelName;
}









