package com.luis.agencia.service;

import com.luis.agencia.dto.PassengerDto;
import com.luis.agencia.model.Passenger;
import java.util.List;

/**
 * Interfaz de servicio para gestionar operaciones relacionadas con pasajeros.
 * Proporciona métodos para crear, actualizar, eliminar y consultar pasajeros,
 * tanto en formato de entidad como en DTO.
 */
public interface IPassengerService {

    /**
     * Retorna la entidad {@link Passenger} correspondiente a un identificador dado.
     * Este método es utilizado internamente por mappers y otros servicios.
     *
     * @param id Identificador del pasajero.
     * @return La entidad {@link Passenger} si se encuentra; de lo contrario, {@code null}.
     */
    Passenger findPassengerById(Long id);

    /**
     * Retorna el DTO de pasajero correspondiente a un identificador dado.
     * Este método es utilizado para exponer la información del pasajero a la capa de presentación.
     *
     * @param id Identificador del pasajero.
     * @return El DTO {@link PassengerDto} si se encuentra; de lo contrario, {@code null}.
     */
    PassengerDto findPassengerDtoById(Long id);

    /**
     * Retorna una lista de DTOs de pasajeros.
     *
     * @return Lista de {@link PassengerDto} con la información de todos los pasajeros.
     */
    List<PassengerDto> listPassengers();

    /**
     * Crea un nuevo pasajero a partir de un DTO y devuelve el DTO resultante.
     *
     * @param passengerDto DTO con la información del pasajero a crear.
     * @return El DTO {@link PassengerDto} del pasajero creado.
     */
    PassengerDto createPassenger(PassengerDto passengerDto);

    /**
     * Actualiza la información de un pasajero existente a partir de un DTO y devuelve el DTO actualizado.
     *
     * @param id             Identificador del pasajero a actualizar.
     * @param passengerDto   DTO con la nueva información del pasajero.
     * @return El DTO {@link PassengerDto} actualizado.
     */
    PassengerDto updatePassenger(Long id, PassengerDto passengerDto);

    /**
     * Elimina físicamente un pasajero de la base de datos.
     *
     * @param id Identificador del pasajero a eliminar.
     */
    void deletePassenger(Long id);

    /**
     * Verifica si existe un pasajero con el identificador proporcionado.
     *
     * @param id Identificador del pasajero.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Guarda o actualiza un pasajero en la base de datos.
     *
     * @param passenger La entidad {@link Passenger} que se desea persistir.
     */
    void save(Passenger passenger);
}



