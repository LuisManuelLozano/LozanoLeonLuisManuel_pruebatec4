package com.luis.agencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un vuelo con información de origen, destino, asientos disponibles,
 * precios, fechas de ida y vuelta, y la lista de reservas de vuelo asociadas.
 *
 * <p>Esta entidad incluye un constructor extenso para mantener compatibilidad
 * con el código existente, aunque supere el límite de parámetros recomendado.
 * Se sugiere, en proyectos nuevos, utilizar patrones como Builder o agrupar
 * parámetros en objetos de valor para mayor legibilidad.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String flightNumber;

    private String origin;
    private String destination;

    private int businessSeatsQ;
    private int economySeatsQ;

    private double businessSeatPrice;
    private double economySeatPrice;

    private boolean isActiva = true;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate dateFrom; // Fecha de ida

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate dateTo;   // Fecha de vuelta

    @JsonIgnore
    @OneToMany(mappedBy = "flight")
    private List<FlightBooking> flightBookings = new ArrayList<>();

    /**
     * Constructor extenso para inicializar todas las propiedades del vuelo.
     * <p>
     * Se mantiene por compatibilidad con código existente, aunque supere el número
     * de parámetros recomendado. Para nuevas implementaciones, se sugiere
     * utilizar un patrón Builder o agrupar parámetros en un objeto de valor.
     *
     * @param name              Nombre único del vuelo.
     * @param flightNumber      Número de vuelo (también único).
     * @param origin            Ciudad de origen.
     * @param destination       Ciudad de destino.
     * @param businessSeatsQ    Cantidad de asientos Business disponibles.
     * @param economySeatsQ     Cantidad de asientos Economy disponibles.
     * @param businessSeatPrice Precio de un asiento Business.
     * @param economySeatPrice  Precio de un asiento Economy.
     * @param isActiva          Indica si el vuelo está activo.
     * @param dateFrom          Fecha de ida.
     * @param dateTo            Fecha de vuelta.
     */
    @SuppressWarnings("java:S107") // Se suprime la advertencia de demasiados parámetros
    public Flight(String name,
                  String flightNumber,
                  String origin,
                  String destination,
                  int businessSeatsQ,
                  int economySeatsQ,
                  double businessSeatPrice,
                  double economySeatPrice,
                  boolean isActiva,
                  LocalDate dateFrom,
                  LocalDate dateTo) {

        this.name = name;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.businessSeatsQ = businessSeatsQ;
        this.economySeatsQ = economySeatsQ;
        this.businessSeatPrice = businessSeatPrice;
        this.economySeatPrice = economySeatPrice;
        this.isActiva = isActiva;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}





