package com.luis.agencia.mapper;

import com.luis.agencia.dto.HotelDto;
import com.luis.agencia.model.Hotel;
import org.junit.jupiter.api.Test;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class HotelMapperTest {

    private final HotelMapper hotelMapper = new HotelMapper();

    @Test
    void testEntityToDto() {
        // Creamos una entidad Hotel con datos reales
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setHotelCode("HOTEL001");
        hotel.setName("Hotel Central");
        hotel.setPlace("Madrid");
        hotel.setSingleRoomsQ(20);
        hotel.setDoubleRoomsQ(10);
        hotel.setSimpleRoomPrice(100.0);
        hotel.setDoubleRoomPrice(150.0);
        hotel.setRooms(Collections.emptyList());

        // Convertimos la entidad a DTO
        HotelDto dto = hotelMapper.entityToDto(hotel);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("HOTEL001", dto.getHotelCode());
        assertEquals("Hotel Central", dto.getName());
        assertEquals("Madrid", dto.getPlace());
        // La lista de rooms se convierte a IDs; al ser vacía, se espera lista vacía
        assertTrue(dto.getRooms().isEmpty());
    }

    @Test
    void testDtoToEntity() {
        // Creamos un DTO Hotel con datos reales
        HotelDto dto = new HotelDto();
        dto.setId(2L);
        dto.setHotelCode("HOTEL002");
        dto.setName("Hotel Costa");
        dto.setPlace("Barcelona");
        dto.setSingleRoomsQ(15);
        dto.setDoubleRoomsQ(8);
        dto.setSimpleRoomPrice(90.0);
        dto.setDoubleRoomPrice(140.0);
        dto.setRooms(Collections.singletonList(100L));

        // Convertimos el DTO a entidad
        Hotel hotel = hotelMapper.dtoToEntity(dto);

        assertNotNull(hotel);
        assertEquals(2L, hotel.getId());
        assertEquals("HOTEL002", hotel.getHotelCode());
        assertEquals("Hotel Costa", hotel.getName());
        assertEquals("Barcelona", hotel.getPlace());
        // El mapper de Hotel no asigna la lista de rooms (se gestiona por separado)
        assertNull(hotel.getRooms());
    }
}



