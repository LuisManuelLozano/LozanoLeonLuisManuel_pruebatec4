package com.luis.agencia.service;

import com.luis.agencia.dto.RoomDto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con habitaciones.
 * Proporciona métodos para crear, actualizar, eliminar y consultar habitaciones,
 * así como para obtener la disponibilidad de habitaciones según distintos criterios.
 */
public interface IroomService {

        /**
         * Retorna una lista de habitaciones disponibles para un hotel en un rango de fechas.
         *
         * @param hotelId  Identificador del hotel.
         * @param fromDate Fecha de inicio de la disponibilidad.
         * @param toDate   Fecha de fin de la disponibilidad.
         * @return Lista de {@link RoomDto} con las habitaciones disponibles.
         */
        List<RoomDto> findAvailableRooms(Long hotelId, LocalDate fromDate, LocalDate toDate);

        /**
         * Crea una nueva habitación a partir de los datos proporcionados en el DTO.
         *
         * @param roomDto DTO con la información de la habitación a crear.
         * @return Mensaje que indica el resultado de la operación.
         */
        String createRoom(RoomDto roomDto);

        /**
         * Actualiza los datos de una habitación existente.
         *
         * @param roomId  Identificador de la habitación a actualizar.
         * @param roomDto DTO con los nuevos datos de la habitación.
         * @return Mensaje que indica el resultado de la operación.
         */
        String updateRoom(Long roomId, RoomDto roomDto);

        /**
         * Elimina una habitación.
         *
         * @param roomId Identificador de la habitación a eliminar.
         * @return Mensaje que indica el resultado de la operación.
         */
        String deleteRoom(Long roomId);

        /**
         * Busca una habitación por su identificador y la retorna en formato DTO.
         *
         * @param roomId Identificador de la habitación.
         * @return El DTO {@link RoomDto} correspondiente, o {@code null} si no se encuentra.
         */
        RoomDto findRoomById(Long roomId);

        /**
         * Retorna una lista de todas las habitaciones existentes en el sistema.
         *
         * @return Lista de {@link RoomDto} con la información de todas las habitaciones.
         */
        List<RoomDto> listAllRooms();

        /**
         * Retorna una lista de habitaciones asociadas a un hotel específico.
         *
         * @param hotelId Identificador del hotel.
         * @return Lista de {@link RoomDto} con las habitaciones del hotel.
         */
        List<RoomDto> listRoomsByHotel(Long hotelId);

        /**
         * Retorna una lista de habitaciones disponibles filtradas por destino en un rango de fechas.
         *
         * @param destination Destino (lugar) del hotel.
         * @param fromDate    Fecha de inicio de la disponibilidad.
         * @param toDate      Fecha de fin de la disponibilidad.
         * @return Lista de {@link RoomDto} con las habitaciones disponibles para el destino especificado.
         */
        List<RoomDto> findAvailableRoomsByDestination(String destination, LocalDate fromDate, LocalDate toDate);

        /**
         * Retorna una lista de habitaciones disponibles filtradas por tipo y destino en un rango de fechas.
         *
         * @param roomType    Tipo de habitación (por ejemplo, SINGLE o DOUBLE).
         * @param fromDate    Fecha de inicio de la disponibilidad.
         * @param toDate      Fecha de fin de la disponibilidad.
         * @param destination Destino (lugar) del hotel.
         * @return Lista de {@link RoomDto} con las habitaciones disponibles que cumplen con los criterios.
         */
        List<RoomDto> findAvailableRoomsByTypeAndDestination(String roomType, LocalDate fromDate, LocalDate toDate, String destination);
}




