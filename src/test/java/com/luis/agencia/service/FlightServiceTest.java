package com.luis.agencia.service;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.mapper.FlightMapper;
import com.luis.agencia.model.Flight;
import com.luis.agencia.repository.IFlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private IFlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightService flightService;

    @Test
    void testCreateFlight_Success() {
        FlightDto dto = new FlightDto();
        dto.setName("Flight Test");
        dto.setFlightNumber("FT001");
        dto.setOrigin("Madrid");
        dto.setDestination("Lisbon");
        dto.setBusinessSeatsQ(5);
        dto.setEconomySeatsQ(20);
        dto.setBusinessSeatPrice(300.0);
        dto.setEconomySeatPrice(150.0);
        dto.setDateFrom(LocalDate.now().plusDays(2));
        dto.setDateTo(LocalDate.now().plusDays(3));
        dto.setActiva(true);

        // Simulamos que no existe otro vuelo con ese número
        when(flightRepository.findByFlightNumber("FT001")).thenReturn(Optional.empty());

        Flight flight = new Flight();
        flight.setId(100L);

        when(flightMapper.dtoToEntity(dto)).thenReturn(flight);
        when(flightRepository.save(flight)).thenReturn(flight);

        // Ejecutamos el método de creación y comprobamos que no lanza excepción
        assertDoesNotThrow(() -> flightService.createFlight(dto));

        // Verificamos que se haya llamado a save() una vez
        verify(flightRepository, times(1)).save(flight);
    }
}

