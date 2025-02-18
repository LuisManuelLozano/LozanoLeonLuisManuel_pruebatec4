package com.luis.agencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa la reserva de un vuelo.
 * Contiene la información del vuelo reservado, la fecha de la reserva,
 * la cantidad de personas involucradas y la lista de pasajeros asociados a la reserva.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FlightBooking {

    /**
     * Identificador único de la reserva de vuelo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Vuelo asociado a la reserva.
     * Se utiliza la relación ManyToOne, ya que un vuelo puede tener múltiples reservas.
     * La anotación @NotNull asegura que siempre se asocie un vuelo a la reserva.
     */
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    @NotNull(message = "El vuelo no puede estar vacío")
    private Flight flight;

    /**
     * Fecha en la que se realiza el vuelo.
     * Se almacena en la base de datos como tipo DATE.
     * La anotación @NotNull asegura que la fecha siempre tenga un valor.
     * La restricción de futuro o presente se ha suprimido para trabajar con datos históricos.
     */
    @Column(columnDefinition = "DATE", nullable = false)
    @NotNull(message = "La fecha del vuelo no puede estar vacía")
    // @FutureOrPresent(message = "La fecha del vuelo debe ser en el presente o futuro")
    private LocalDate date;

    /**
     * Cantidad de personas para las que se realiza la reserva.
     * Se valida que el número mínimo de personas sea 1.
     */
    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    private int peopleQ;

    /**
     * Lista de pasajeros asociados a la reserva de vuelo.
     * Se utiliza la relación OneToMany, donde un vuelo puede tener múltiples pasajeros.
     * La anotación @JsonIgnore evita la serialización de esta lista en respuestas JSON para prevenir
     * problemas de recursión infinita o información redundante.
     */
    @OneToMany(mappedBy = "flightBooking")
    @JsonIgnore
    private List<Passenger> passengers = new ArrayList<>();
}






