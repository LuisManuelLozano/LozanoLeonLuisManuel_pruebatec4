package com.luis.agencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entidad Pasajero (Passenger) - Para ambos tipos de reserva
//Passenger tiene relaciones ManyToOne con RoomBooking y FlightBooking.
// Un pasajero puede estar asociado con una reserva de habitación y una reserva de vuelo.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    // Relación ManyToOne con RoomBooking
    // Un pasajero puede estar asociado con una reserva de habitación
    //muchos Passenger pueden estar asociados con una sola RoomBooking
    @ManyToOne
    //el nombre de la columna que contendrá la clave foránea en la tabla Passenger es room_booking_id
    @JoinColumn(name = "room_booking_id")
    private RoomBooking roomBooking;
    // Relación ManyToOne con FlightBooking
    // varios pasajeros pueden estar vinculados a una misma reserva de vuelo.
    @ManyToOne
    //la columna de la base de datos que se utilizará para unir esta entidad (Passenger) con la entidad FlightBooking.
    @JoinColumn(name = "flight_booking_id")
    private FlightBooking flightBooking;
}

