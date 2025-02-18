package com.luis.agencia.mapper;

import com.luis.agencia.dto.RoomBookingDto;
import com.luis.agencia.dto.RoomBookingDetailDto;
import com.luis.agencia.model.Passenger;
import com.luis.agencia.model.Room;
import com.luis.agencia.model.RoomBooking;
import com.luis.agencia.model.RoomType;
import com.luis.agencia.service.IPassengerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre la entidad {@link RoomBooking} y sus representaciones en DTO:
 * {@link RoomBookingDto} y {@link RoomBookingDetailDto}.
 * <p>
 * Se encarga de transformar listas de entidades (por ejemplo, {@link Passenger} y {@link Room})
 * en listas de identificadores o valores calculados.
 * <br><br>
 * <b>Nota:</b> Debido a que {@link RoomBookingDetailDto} no contiene el campo de IDs de pasajeros,
 * el método {@code detailDtoToEntity} no asigna pasajeros a la entidad.
 */
@Component
@SuppressWarnings("unused") // Suprime los avisos de clase/métodos no usados
public class RoomBookingMapper {

    private final IPassengerService passengerService;

    /**
     * Constructor que inyecta el servicio de pasajeros para poder convertir IDs a entidades.
     *
     * @param passengerService Servicio para la gestión de pasajeros.
     */
    public RoomBookingMapper(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Convierte una entidad {@link RoomBooking} a un DTO {@link RoomBookingDto}.
     *
     * @param roomBooking La entidad de reserva de habitación a convertir.
     * @return El DTO correspondiente, o {@code null} si la entidad es {@code null}.
     */
    public RoomBookingDto entityToDto(RoomBooking roomBooking) {
        if (roomBooking == null) {
            return null;
        }
        RoomBookingDto dto = new RoomBookingDto();
        dto.setId(roomBooking.getId());
        dto.setDateFrom(roomBooking.getDateFrom());
        dto.setDateTo(roomBooking.getDateTo());
        dto.setNights(roomBooking.getNights());
        dto.setPeopleQ(roomBooking.getPeopleQ());

        // Convertir la lista de Passenger a lista de IDs
        List<Long> passengerIds = roomBooking.getPassengers().stream()
                .map(Passenger::getId)
                .toList(); // Requiere Java 16+
        dto.setPassengersIds(passengerIds);

        // Si la lista de Rooms no está vacía, se extrae el nombre del hotel del primer room
        List<Room> rooms = roomBooking.getRooms();
        if (!rooms.isEmpty()) {
            dto.setHotelName(rooms.get(0).getHotel().getName());
        }

        return dto;
    }

    /**
     * Convierte un DTO {@link RoomBookingDto} a una entidad {@link RoomBooking}.
     *
     * @param dto El DTO que contiene la información de la reserva.
     * @return La entidad {@link RoomBooking} resultante, o {@code null} si el DTO es {@code null}.
     */
    public RoomBooking dtoToEntity(RoomBookingDto dto) {
        if (dto == null) {
            return null;
        }
        RoomBooking booking = new RoomBooking();
        booking.setId(dto.getId());
        booking.setDateFrom(dto.getDateFrom());
        booking.setDateTo(dto.getDateTo());
        booking.setNights(dto.getNights());
        booking.setPeopleQ(dto.getPeopleQ());

        // Convertir la lista de IDs a entidades Passenger
        List<Passenger> passengers = dto.getPassengersIds().stream()
                .map(passengerService::findPassengerById)
                .toList();
        booking.setPassengers(passengers);

        return booking;
    }

    /**
     * Convierte una entidad {@link RoomBooking} a un DTO de detalle {@link RoomBookingDetailDto}.
     *
     * @param roomBooking La entidad de reserva a convertir.
     * @return El DTO de detalle correspondiente, o {@code null} si la entidad es {@code null}.
     */
    public RoomBookingDetailDto entityToDetailDto(RoomBooking roomBooking) {
        if (roomBooking == null) {
            return null;
        }
        RoomBookingDetailDto detail = new RoomBookingDetailDto();
        detail.setId(roomBooking.getId());
        detail.setDateFrom(roomBooking.getDateFrom());
        detail.setDateTo(roomBooking.getDateTo());
        detail.setNights(roomBooking.getNights());
        detail.setPeopleQ(roomBooking.getPeopleQ());

        // Calcular el costo total en base a las habitaciones y el número de noches
        List<Room> rooms = roomBooking.getRooms();
        double totalCost = rooms.stream()
                .mapToDouble(room -> {
                    if (room.getRoomType() == RoomType.DOUBLE) {
                        return room.getHotel().getDoubleRoomPrice();
                    } else {
                        return room.getHotel().getSimpleRoomPrice();
                    }
                })
                .sum() * roomBooking.getNights();
        detail.setTotalCost(totalCost);

        // Si hay habitaciones, obtener el nombre del hotel y el destino del primer room
        if (!rooms.isEmpty()) {
            Room firstRoom = rooms.get(0);
            detail.setHotelName(firstRoom.getHotel().getName());
            detail.setDestination(firstRoom.getHotel().getPlace());
        }

        // Filtrar las IDs de habitaciones dobles
        List<Long> doubleIds = rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.DOUBLE)
                .map(Room::getId)
                .toList();
        // Filtrar las IDs de habitaciones simples
        List<Long> singleIds = rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.SINGLE)
                .map(Room::getId)
                .toList();

        detail.setReservedDoubleRoomIds(doubleIds);
        detail.setReservedSingleRoomIds(singleIds);

        return detail;
    }

    /**
     * Convierte un DTO de detalle {@link RoomBookingDetailDto} a una entidad {@link RoomBooking}.
     * <p>
     * Dado que {@link RoomBookingDetailDto} no contiene la lista de pasajeros, este método
     * no asigna pasajeros a la entidad. Si se requiere, habría que añadir la información de pasajeros
     * en el DTO de detalle.
     *
     * @param detailDto El DTO de detalle de la reserva.
     * @return La entidad {@link RoomBooking} resultante, o {@code null} si el DTO es {@code null}.
     */
    public RoomBooking detailDtoToEntity(RoomBookingDetailDto detailDto) {
        if (detailDto == null) {
            return null;
        }
        RoomBooking booking = new RoomBooking();
        booking.setId(detailDto.getId());
        booking.setDateFrom(detailDto.getDateFrom());
        booking.setDateTo(detailDto.getDateTo());
        booking.setNights(detailDto.getNights());
        booking.setPeopleQ(detailDto.getPeopleQ());
        // No se asignan pasajeros ni habitaciones, ya que detailDto no los provee.
        return booking;
    }
}






