package com.luis.agencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información de un hotel.
 * Contiene datos básicos del hotel, como código, nombre, ubicación,
 * cantidad de habitaciones y precios, además de la lista de identificadores de habitaciones asociadas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    /**
     * Identificador único del hotel.
     */
    private Long id;

    /**
     * Código único del hotel.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El código del hotel no puede estar vacío")
    private String hotelCode;

    /**
     * Nombre del hotel.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El nombre del hotel no puede estar vacío")
    private String name;

    /**
     * Ubicación o lugar del hotel.
     * Este campo no puede estar vacío.
     */
    @NotBlank(message = "El lugar no puede estar vacío")
    private String place;

    /**
     * Número de habitaciones individuales disponibles.
     * Debe ser un número positivo.
     */
    @Positive(message = "El número de habitaciones individuales debe ser positivo")
    private int singleRoomsQ;

    /**
     * Número de habitaciones dobles disponibles.
     * Debe ser un número positivo.
     */
    @Positive(message = "El número de habitaciones dobles debe ser positivo")
    private int doubleRoomsQ;

    /**
     * Precio por noche de una habitación individual.
     * Debe ser un número positivo.
     */
    @Positive(message = "El precio de la habitación debe ser positivo")
    private double simpleRoomPrice;

    /**
     * Precio por noche de una habitación doble.
     * Debe ser un número positivo.
     */
    @Positive(message = "El precio de la habitación debe ser positivo")
    private double doubleRoomPrice;

    /**
     * Lista de identificadores de habitaciones asociadas al hotel.
     * (Si se utiliza para relacionar con la entidad Room).
     */
    private List<Long> rooms;
}




