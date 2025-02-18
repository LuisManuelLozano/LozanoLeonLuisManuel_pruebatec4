package com.luis.agencia.service;

import com.luis.agencia.dto.RoomDto;
import com.luis.agencia.mapper.RoomMapper;
import com.luis.agencia.model.Room;
import com.luis.agencia.model.RoomType;
import com.luis.agencia.repository.IRoomRepository;
import com.luis.agencia.repository.IHotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private IRoomRepository roomRepository;

    @Mock
    private IHotelRepository hotelRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void testCreateRoom_Success() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(10L);
        roomDto.setRoomType(RoomType.SINGLE);
        roomDto.setHotelId(5L);
        roomDto.setDisponibilityDateFrom(LocalDate.now());
        roomDto.setDisponibilityDateTo(LocalDate.now().plusDays(10));

        // Simulamos que el hotel existe
        when(hotelRepository.existsById(5L)).thenReturn(true);

        Room room = new Room();
        room.setId(10L);

        when(roomMapper.dtoToEntity(roomDto)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);

        String result = roomService.createRoom(roomDto);
        assertEquals("Room created successfully.", result);
        verify(roomRepository, times(1)).save(room);
    }
}


