package com.luis.agencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un hotel dentro de la agencia de viajes.
 * Contiene información como el código, nombre, ubicación,
 * número de habitaciones (simples y dobles) y su precio.
 * Además, mantiene una relación OneToMany con la entidad Room.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {

    /**
     * Identificador único del hotel.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código único del hotel, utilizado para su identificación.
     * Debe ser único y no puede ser nulo.
     */
    @Column(unique = true, nullable = false)
    private String hotelCode;

    /**
     * Nombre del hotel.
     */
    private String name;

    /**
     * Ubicación o lugar donde se encuentra el hotel.
     */
    private String place;

    /**
     * Cantidad de habitaciones simples disponibles en el hotel.
     */
    private int singleRoomsQ;

    /**
     * Cantidad de habitaciones dobles disponibles en el hotel.
     */
    private int doubleRoomsQ;

    /**
     * Precio por noche de la habitación doble.
     */
    private double doubleRoomPrice;

    /**
     * Precio por noche de la habitación simple.
     */
    private double simpleRoomPrice;

    /**
     * Indica si el hotel está activo o disponible.
     */
    private boolean isActive = true;

    /**
     * Relación OneToMany con la entidad Room.
     * Se ignora en la serialización JSON para evitar problemas de recursión
     * o datos redundantes.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
}














