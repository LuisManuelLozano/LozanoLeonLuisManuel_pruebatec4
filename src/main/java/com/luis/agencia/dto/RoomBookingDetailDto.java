package com.luis.agencia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO que representa el detalle de una reserva de habitación.
 * Contiene información sobre el período de la reserva, la cantidad de noches,
 * número de personas, costo total y detalles adicionales del hotel y habitaciones reservadas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingDetailDto {

    /**
     * Identificador único de la reserva.
     */
    private Long id;

    /**
     * Fecha de inicio de la reserva.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateFrom;

    /**
     * Fecha de fin de la reserva.
     * Se formatea como "dd-MM-yyyy" en la representación JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateTo;

    /**
     * Número de noches que dura la reserva.
     */
    private int nights;

    /**
     * Cantidad de personas para las que se realizó la reserva.
     */
    private int peopleQ;

    /**
     * Costo total de la reserva.
     */
    private double totalCost;

    /**
     * Nombre del hotel asociado a la reserva.
     */
    private String hotelName;

    /**
     * Destino del hotel.
     */
    private String destination;

    /**
     * Lista de identificadores de habitaciones dobles reservadas.
     */
    private List<Long> reservedDoubleRoomIds;

    /**
     * Lista de identificadores de habitaciones individuales reservadas.
     */
    private List<Long> reservedSingleRoomIds;
}

