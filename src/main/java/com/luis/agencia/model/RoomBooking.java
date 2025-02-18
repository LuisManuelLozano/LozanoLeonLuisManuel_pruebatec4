package com.luis.agencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una reserva de habitaciones (RoomBooking) en la agencia de viajes.
 * Contiene la información de las fechas de reserva, cantidad de noches, número de personas,
 * y las relaciones con las habitaciones (Room) y pasajeros (Passenger) asociados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomBooking {

    /**
     * Identificador único de la reserva de habitación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de inicio de la reserva.
     * Se almacena en la base de datos como DATE y no puede ser nula.
     */
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate dateFrom;

    /**
     * Fecha de finalización de la reserva.
     * Se almacena en la base de datos como DATE y no puede ser nula.
     */
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate dateTo;

    /**
     * Número de noches que abarca la reserva.
     */
    private int nights;

    /**
     * Cantidad de personas para las que se realiza la reserva.
     */
    private int peopleQ;

    /**
     * Relación OneToMany con la entidad Room.
     * Se configuran las operaciones de cascada PERSIST, MERGE y REFRESH para evitar que
     * las habitaciones sean eliminadas físicamente al borrar la reserva.
     */
    @OneToMany(mappedBy = "roomBooking",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Room> rooms = new ArrayList<>();

    /**
     * Relación OneToMany con la entidad Passenger.
     * Se configuran las operaciones de cascada PERSIST, MERGE y REFRESH para evitar que
     * los pasajeros sean eliminados físicamente al borrar la reserva.
     */
    @OneToMany(mappedBy = "roomBooking",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Passenger> passengers = new ArrayList<>();
}





