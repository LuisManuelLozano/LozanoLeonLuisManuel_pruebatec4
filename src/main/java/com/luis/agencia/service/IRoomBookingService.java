package com.luis.agencia.service;

import com.luis.agencia.dto.RoomBookingDto;
import com.luis.agencia.dto.RoomBookingDetailDto;
import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con reservas de habitaciones.
 * Proporciona métodos para crear, actualizar, eliminar reservas y obtener detalles de las mismas.
 */
public interface IRoomBookingService {

    /**
     * Crea una nueva reserva de habitación a partir del DTO proporcionado.
     *
     * @param roomBookingDto DTO que contiene la información de la reserva.
     * @return El DTO {@link RoomBookingDto} de la reserva creada.
     */
    RoomBookingDto createRoomBooking(RoomBookingDto roomBookingDto);

    /**
     * Actualiza una reserva de habitación existente.
     *
     * @param id              Identificador de la reserva a actualizar.
     * @param roomBookingDto  DTO que contiene la información actualizada de la reserva.
     * @return El DTO {@link RoomBookingDto} de la reserva actualizada.
     */
    RoomBookingDto updateRoomBooking(Long id, RoomBookingDto roomBookingDto);

    /**
     * Elimina una reserva de habitación.
     *
     * @param id Identificador de la reserva a eliminar.
     */
    void deleteRoomBooking(Long id);

    /**
     * Retorna una lista con los detalles de todas las reservas de habitación existentes.
     *
     * @return Lista de {@link RoomBookingDetailDto} con la información de cada reserva.
     */
    List<RoomBookingDetailDto> listAllRoomBookings();

    /**
     * Obtiene los detalles de una reserva de habitación a partir de su identificador.
     *
     * @param id Identificador de la reserva.
     * @return El DTO {@link RoomBookingDetailDto} con los detalles de la reserva, o {@code null} si no se encuentra.
     */
    RoomBookingDetailDto getRoomBookingDetailById(Long id);
}




