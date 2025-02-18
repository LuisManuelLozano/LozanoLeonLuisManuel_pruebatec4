package com.luis.agencia.mapper;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.model.Flight;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlightMapperTest {

    private final FlightMapper flightMapper = new FlightMapper(new ModelMapper());

    @Test
    void testEntityToDto() {
        // Creamos una entidad Flight con datos reales
        Flight flight = new Flight();
        flight.setName("Flight A101");
        flight.setFlightNumber("F101");
        flight.setOrigin("Madrid");
        flight.setDestination("Paris");
        flight.setBusinessSeatsQ(10);
        flight.setEconomySeatsQ(50);
        flight.setBusinessSeatPrice(250.0);
        flight.setEconomySeatPrice(150.0);
        flight.setDateFrom(LocalDate.now().plusDays(1));
        flight.setDateTo(LocalDate.now().plusDays(2));
        flight.setActiva(true);

        // Convertimos la entidad en DTO
        FlightDto dto = flightMapper.entityToDto(flight);

        assertNotNull(dto);
        assertEquals("Flight A101", dto.getName());
        assertEquals("F101", dto.getFlightNumber());
        assertEquals("Madrid", dto.getOrigin());
        assertEquals("Paris", dto.getDestination());
        assertTrue(dto.isActiva());
    }

    @Test
    void testDtoToEntity() {
        // Creamos un DTO FlightDto con datos reales (sin campo id)
        FlightDto dto = new FlightDto();
        dto.setName("Flight B202");
        dto.setFlightNumber("F202");
        dto.setOrigin("London");
        dto.setDestination("New York");
        dto.setBusinessSeatsQ(8);
        dto.setEconomySeatsQ(40);
        dto.setBusinessSeatPrice(300.0);
        dto.setEconomySeatPrice(200.0);
        dto.setDateFrom(LocalDate.now().plusDays(3));
        dto.setDateTo(LocalDate.now().plusDays(4));
        dto.setActiva(true);

        // Convertimos el DTO en entidad
        Flight flight = flightMapper.dtoToEntity(dto);

        assertNotNull(flight);
        assertEquals("Flight B202", flight.getName());
        assertEquals("F202", flight.getFlightNumber());
        assertEquals("London", flight.getOrigin());
        assertEquals("New York", flight.getDestination());
        assertTrue(flight.isActiva());
    }
}


