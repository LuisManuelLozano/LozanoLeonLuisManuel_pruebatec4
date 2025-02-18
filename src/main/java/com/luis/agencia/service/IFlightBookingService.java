package com.luis.agencia.service;

import com.luis.agencia.dto.FlightBookingDetailDto;
import com.luis.agencia.dto.FlightBookingDto;
import com.luis.agencia.model.FlightBooking;

import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con reservas de vuelo.
 * Proporciona métodos para crear, editar, eliminar y consultar reservas de vuelo.
 */
public interface IFlightBookingService {

    /**
     * Edita una reserva de vuelo existente.
     *
     * @param id  Identificador de la reserva que se desea editar.
     * @param dto DTO que contiene la información actualizada de la reserva.
     * @return Un {@link FlightBookingDetailDto} con los detalles actualizados de la reserva.
     */
    FlightBookingDetailDto edit(Long id, FlightBookingDto dto);

    /**
     * Crea una nueva reserva de vuelo.
     *
     * @param dto DTO que contiene la información de la nueva reserva.
     * @return Un {@link FlightBookingDetailDto} con los detalles de la reserva creada.
     */
    FlightBookingDetailDto create(FlightBookingDto dto);

    /**
     * Elimina una reserva de vuelo.
     *
     * @param id Identificador de la reserva que se desea eliminar.
     */
    void delete(Long id);

    /**
     * Busca una reserva de vuelo por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return La entidad {@link FlightBooking} encontrada, o {@code null} si no se encuentra.
     */
    FlightBooking findById(Long id);

    /**
     * Retorna una lista con todas las reservas de vuelo existentes.
     *
     * @return Lista de entidades {@link FlightBooking}.
     */
    List<FlightBooking> findAll();
}

