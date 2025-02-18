package com.luis.agencia.service;

import com.luis.agencia.dto.HotelDto;
import com.luis.agencia.model.Hotel;
import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con hoteles.
 * Proporciona métodos para crear, editar, eliminar y consultar hoteles.
 */
public interface IHotelService {

    /**
     * Busca un hotel por su identificador.
     *
     * @param id Identificador del hotel.
     * @return La entidad {@link Hotel} encontrada, o {@code null} si no se encuentra.
     */
    Hotel findHotelById(Long id);

    /**
     * Busca un hotel por su identificador y lo retorna en formato DTO.
     *
     * @param id Identificador del hotel.
     * @return El DTO {@link HotelDto} del hotel encontrado, o {@code null} si no se encuentra.
     */
    HotelDto findHotelDtoById(Long id);

    /**
     * Crea un nuevo hotel en la base de datos.
     *
     * @param hotelDto DTO que contiene la información del hotel a crear.
     * @return Un mensaje indicando el resultado de la operación (por ejemplo, éxito o error).
     */
    String createHotel(HotelDto hotelDto);

    /**
     * Elimina un hotel de la base de datos de forma lógica o física, según la implementación.
     *
     * @param id Identificador del hotel a eliminar.
     * @return Un mensaje indicando el resultado de la eliminación.
     */
    String deleteHotel(Long id);

    /**
     * Edita la información de un hotel existente.
     *
     * @param id       Identificador del hotel a editar.
     * @param hotelDto DTO que contiene la información actualizada del hotel.
     * @return Un mensaje indicando el resultado de la operación (por ejemplo, éxito o error).
     */
    String editHotel(Long id, HotelDto hotelDto);

    /**
     * Retorna una lista con todos los hoteles existentes, en formato DTO.
     *
     * @return Lista de {@link HotelDto} con la información de los hoteles.
     */
    List<HotelDto> listHotels();
}

