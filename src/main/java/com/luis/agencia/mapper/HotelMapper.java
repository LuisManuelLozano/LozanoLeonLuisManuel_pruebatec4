package com.luis.agencia.mapper;

import com.luis.agencia.dto.HotelDto;
import com.luis.agencia.model.Hotel;
import com.luis.agencia.model.Room;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad {@link Hotel} y su representación en DTO {@link HotelDto}.
 * Además, se realiza el mapeo de la lista de habitaciones asociadas.
 */
@Component
public class HotelMapper {

    /**
     * Convierte una entidad {@link Hotel} a un DTO {@link HotelDto}.
     *
     * @param hotel La entidad de hotel a convertir.
     * @return El DTO correspondiente con la información del hotel, o {@code null} si la entidad es {@code null}.
     */
    public HotelDto entityToDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(hotel.getId());
        hotelDto.setHotelCode(hotel.getHotelCode());
        hotelDto.setName(hotel.getName());
        hotelDto.setPlace(hotel.getPlace());
        hotelDto.setDoubleRoomsQ(hotel.getDoubleRoomsQ());
        hotelDto.setSingleRoomsQ(hotel.getSingleRoomsQ());
        hotelDto.setDoubleRoomPrice(hotel.getDoubleRoomPrice());
        hotelDto.setSimpleRoomPrice(hotel.getSimpleRoomPrice());
        // Mapeo de habitaciones: extrae solo los IDs de las habitaciones asociadas.
        hotelDto.setRooms(hotel.getRooms().stream().map(Room::getId).toList());
        return hotelDto;
    }

    /**
     * Convierte un DTO {@link HotelDto} a su entidad correspondiente {@link Hotel}.
     *
     * @param hotelDto El DTO que contiene la información del hotel.
     * @return La entidad {@link Hotel} resultante, o {@code null} si el DTO es {@code null}.
     */
    public Hotel dtoToEntity(HotelDto hotelDto) {
        if (hotelDto == null) {
            return null;
        }
        Hotel hotel = new Hotel();
        hotel.setId(hotelDto.getId());
        hotel.setHotelCode(hotelDto.getHotelCode());
        hotel.setName(hotelDto.getName());
        hotel.setPlace(hotelDto.getPlace());
        hotel.setDoubleRoomsQ(hotelDto.getDoubleRoomsQ());
        hotel.setSingleRoomsQ(hotelDto.getSingleRoomsQ());
        hotel.setDoubleRoomPrice(hotelDto.getDoubleRoomPrice());
        hotel.setSimpleRoomPrice(hotelDto.getSimpleRoomPrice());
        // No se mapea la lista de habitaciones, ya que se gestiona por separado.
        return hotel;
    }
}


