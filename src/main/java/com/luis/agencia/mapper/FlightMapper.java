package com.luis.agencia.mapper;

import com.luis.agencia.dto.FlightDto;
import com.luis.agencia.model.Flight;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {

    private final ModelMapper modelMapper;

    public FlightMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public FlightDto entityToDto(Flight flight) {
        if (flight == null) {
            return null;
        }
        return modelMapper.map(flight, FlightDto.class);
    }

    public Flight dtoToEntity(FlightDto flightDto) {
        if (flightDto == null) {
            return null;
        }
        return modelMapper.map(flightDto, Flight.class);
    }
}

