package com.luis.agencia.mapper;

import com.luis.agencia.dto.PassengerDto;
import com.luis.agencia.model.Passenger;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad {@link Passenger} y su representación en DTO {@link PassengerDto}.
 */
@Component
public class PassengerMapper {

    /**
     * Convierte una entidad {@link Passenger} a un DTO {@link PassengerDto}.
     *
     * @param passenger La entidad de pasajero a convertir.
     * @return El DTO correspondiente, o {@code null} si la entidad es {@code null}.
     */
    public PassengerDto entityToDto(Passenger passenger) {
        if (passenger == null) {
            return null;
        }
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setId(passenger.getId());
        passengerDto.setName(passenger.getName());
        passengerDto.setLastName(passenger.getLastName());
        passengerDto.setDni(passenger.getDni());
        return passengerDto;
    }

    /**
     * Convierte un DTO {@link PassengerDto} a su entidad correspondiente {@link Passenger}.
     *
     * @param passengerDto El DTO que contiene la información del pasajero.
     * @return La entidad {@link Passenger} resultante, o {@code null} si el DTO es {@code null}.
     */
    public Passenger dtoToEntity(PassengerDto passengerDto) {
        if (passengerDto == null) {
            return null;
        }
        Passenger passenger = new Passenger();
        passenger.setId(passengerDto.getId());
        passenger.setName(passengerDto.getName());
        passenger.setLastName(passengerDto.getLastName());
        passenger.setDni(passengerDto.getDni());
        return passenger;
    }
}

