package com.luis.agencia.mapper;

import com.luis.agencia.dto.FlightBookingDetailDto;
import com.luis.agencia.dto.FlightBookingDto;
import com.luis.agencia.model.FlightBooking;
import com.luis.agencia.model.Passenger;
import com.luis.agencia.service.IPassengerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre la entidad {@link FlightBooking} y sus
 * representaciones en DTO: {@link FlightBookingDto} y {@link FlightBookingDetailDto}.
 * Utiliza el servicio {@link IPassengerService} para la conversión de IDs de pasajeros.
 */
@Component
@SuppressWarnings("unused") // Suprime los warnings de "clase/métodos no usados"
public class FlightBookingMapper {

    private final IPassengerService passengerService;

    /**
     * Inyección de dependencias por constructor.
     *
     * @param passengerService Servicio para la gestión de pasajeros, utilizado para obtener la entidad Passenger a partir de un ID.
     */
    public FlightBookingMapper(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Convierte una entidad {@link FlightBooking} a un {@link FlightBookingDto}.
     *
     * @param flightBooking La entidad de reserva de vuelo a convertir.
     * @return El DTO correspondiente con la fecha, cantidad de personas y los IDs de los pasajeros.
     */
    public FlightBookingDto entityToDto(FlightBooking flightBooking) {
        if (flightBooking == null) {
            return null;
        }
        FlightBookingDto dto = new FlightBookingDto();
        dto.setDate(flightBooking.getDate());
        dto.setPeopleQ(flightBooking.getPeopleQ());
        // Obtiene los IDs de los pasajeros asociados a la reserva.
        List<Long> passengerIds = flightBooking.getPassengers().stream()
                .map(Passenger::getId)
                .toList();
        dto.setPassengersIds(passengerIds);
        return dto;
    }

    /**
     * Convierte un {@link FlightBookingDto} a una entidad {@link FlightBooking}.
     *
     * @param flightBookingDto El DTO que contiene la información de la reserva.
     * @return La entidad {@link FlightBooking} resultante, con la fecha, cantidad de personas y los pasajeros cargados.
     */
    public FlightBooking dtoToEntity(FlightBookingDto flightBookingDto) {
        if (flightBookingDto == null) {
            return null;
        }
        FlightBooking flightBooking = new FlightBooking();
        flightBooking.setDate(flightBookingDto.getDate());
        flightBooking.setPeopleQ(flightBookingDto.getPeopleQ());
        // Convierte la lista de IDs de pasajeros en una lista de entidades Passenger.
        List<Passenger> passengers = flightBookingDto.getPassengersIds().stream()
                .map(passengerService::findPassengerById)
                .toList();
        flightBooking.setPassengers(passengers);
        return flightBooking;
    }

    /**
     * Convierte una entidad {@link FlightBooking} a un {@link FlightBookingDetailDto}.
     *
     * @param flightBooking La entidad de reserva de vuelo a convertir.
     * @return El DTO de detalle correspondiente con la fecha, cantidad de personas y los IDs de los pasajeros.
     */
    public FlightBookingDetailDto entityToDetailDto(FlightBooking flightBooking) {
        if (flightBooking == null) {
            return null;
        }
        FlightBookingDetailDto detailDto = new FlightBookingDetailDto();
        detailDto.setDate(flightBooking.getDate());
        detailDto.setPeopleQ(flightBooking.getPeopleQ());
        // Obtiene los IDs de los pasajeros asociados a la reserva.
        List<Long> passengerIds = flightBooking.getPassengers().stream()
                .map(Passenger::getId)
                .toList();
        detailDto.setPassengersIds(passengerIds);
        return detailDto;
    }

    /**
     * Convierte un {@link FlightBookingDetailDto} a una entidad {@link FlightBooking}.
     *
     * @param detailDto El DTO de detalle de reserva.
     * @return La entidad {@link FlightBooking} resultante, con la fecha, cantidad de personas y los pasajeros cargados.
     */
    public FlightBooking detailDtoToEntity(FlightBookingDetailDto detailDto) {
        if (detailDto == null) {
            return null;
        }
        FlightBooking flightBooking = new FlightBooking();
        flightBooking.setDate(detailDto.getDate());
        flightBooking.setPeopleQ(detailDto.getPeopleQ());
        // Convierte la lista de IDs de pasajeros en una lista de entidades Passenger.
        List<Passenger> passengers = detailDto.getPassengersIds().stream()
                .map(passengerService::findPassengerById)
                .toList();
        flightBooking.setPassengers(passengers);
        return flightBooking;
    }
}

