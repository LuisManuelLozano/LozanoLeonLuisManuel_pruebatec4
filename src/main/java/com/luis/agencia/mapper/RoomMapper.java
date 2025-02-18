package com.luis.agencia.mapper;

import com.luis.agencia.dto.RoomDto;
import com.luis.agencia.model.Room;
import com.luis.agencia.repository.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre la entidad {@link Room} y su representación en DTO {@link RoomDto}.
 * Utiliza el repositorio de hoteles ({@link IHotelRepository}) para asignar la entidad Hotel
 * a la habitación cuando se convierte desde un DTO.
 */
@Component
public class RoomMapper {

    /**
     * Repositorio para acceder a la información de hoteles,
     * necesario para asignar la entidad Hotel a la habitación.
     */
    @Autowired
    private IHotelRepository hotelRepository;

    /**
     * Convierte una entidad {@link Room} en un DTO {@link RoomDto}.
     *
     * @param room La entidad de habitación a convertir.
     * @return El DTO correspondiente, o {@code null} si la entidad es {@code null}.
     */
    public RoomDto entityToDto(Room room) {
        if (room == null) {
            return null;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setHotelId(room.getHotel().getId());
        roomDto.setDisponibilityDateFrom(room.getDisponibilityDateFrom());
        roomDto.setDisponibilityDateTo(room.getDisponibilityDateTo());
        return roomDto;
    }

    /**
     * Convierte un DTO {@link RoomDto} en una entidad {@link Room}.
     * Se obtiene la entidad Hotel correspondiente a partir del ID proporcionado en el DTO.
     *
     * @param roomDto El DTO que contiene la información de la habitación.
     * @return La entidad {@link Room} resultante, o {@code null} si el DTO es {@code null}.
     */
    public Room dtoToEntity(RoomDto roomDto) {
        if (roomDto == null) {
            return null;
        }
        Room room = new Room();
        room.setId(roomDto.getId());
        room.setRoomType(roomDto.getRoomType());
        room.setDisponibilityDateFrom(roomDto.getDisponibilityDateFrom());
        room.setDisponibilityDateTo(roomDto.getDisponibilityDateTo());
        room.setHotel(hotelRepository.findById(roomDto.getHotelId()).orElse(null));
        return room;
    }
}
