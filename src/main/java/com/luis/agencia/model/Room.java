package com.luis.agencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidad que representa una habitación (Room) dentro de la agencia de viajes.
 * Cada habitación se asocia a un tipo específico (RoomType), un intervalo de disponibilidad
 * y está vinculada a un hotel y, opcionalmente, a una reserva de habitación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    /**
     * Identificador único de la habitación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo de la habitación (por ejemplo, SIMPLE o DOBLE),
     * definido en el enum {@link RoomType}.
     */
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    /**
     * Fecha a partir de la cual la habitación está disponible.
     * Se almacena como DATE en la base de datos y se formatea según el patrón "dd-MM-yyyy".
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate disponibilityDateFrom;

    /**
     * Fecha hasta la cual la habitación está disponible.
     * Se almacena como DATE en la base de datos y se formatea según el patrón "dd-MM-yyyy".
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate disponibilityDateTo;

    /**
     * Hotel al que pertenece la habitación.
     * Relación ManyToOne, ya que un hotel puede tener múltiples habitaciones.
     * Se ignora en la serialización JSON para evitar ciclos de referencia.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    /**
     * Reserva de habitación a la que está asociada esta habitación (opcional).
     * Relación ManyToOne, ya que una habitación solo puede estar en una reserva a la vez.
     * Se ignora en la serialización JSON para evitar ciclos de referencia.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_booking_id")
    private RoomBooking roomBooking;
}

