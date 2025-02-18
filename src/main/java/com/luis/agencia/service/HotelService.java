package com.luis.agencia.service;

import com.luis.agencia.dto.HotelDto;
import com.luis.agencia.mapper.HotelMapper;
import com.luis.agencia.model.Hotel;
import com.luis.agencia.repository.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;


/**
 * Servicio que gestiona la lógica relacionada con los hoteles, incluyendo
 * creación, edición, eliminación lógica y obtención de hoteles en formato DTO.
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class HotelService implements IHotelService {

    @Autowired
    private IHotelRepository hotelRepository;

    @Autowired
    private HotelMapper hotelMapper;

    /**
     * Busca un hotel por su identificador.
     *
     * @param id Identificador del hotel.
     * @return La entidad {@link Hotel} encontrada, o {@code null} si no se encuentra.
     */
    @Override
    @Transactional
    public Hotel findHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    /**
     * Busca un hotel por su identificador y lo devuelve en forma de DTO.
     *
     * @param id Identificador del hotel.
     * @return El DTO {@link HotelDto} del hotel encontrado, o {@code null} en caso contrario.
     */
    @Override
    @Transactional
    public HotelDto findHotelDtoById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel == null) {
            return null;
        }
        HotelDto hotelDto = hotelMapper.entityToDto(hotel);
        return hotel.isActive() ? hotelDto : null;
    }

    /**
     * Crea un nuevo hotel en la base de datos a partir de un DTO.
     *
     * @param hotelDto DTO que contiene la información del hotel a crear.
     * @return Un mensaje indicando el resultado de la operación.
     * @throws IllegalArgumentException si el DTO es nulo o si ya existe un hotel con el mismo código.
     */
    @Override
    @Transactional
    public String createHotel(HotelDto hotelDto) {
        if (hotelDto == null) {
            return "No se puede añadir un hotel vacío";
        }
        if (hotelRepository.findByHotelCode(hotelDto.getHotelCode()).isPresent()) {
            return "No se puede añadir el hotel porque ya existe ese HotelCode";
        }
        if (hotelDto.getHotelCode().isEmpty() || hotelDto.getName().isEmpty() || hotelDto.getPlace().isEmpty()) {
            return "No se pueden añadir campos vacíos";
        }
        // Validaciones adicionales (opcional)
        if (hotelDto.getSimpleRoomPrice() < 0 || hotelDto.getDoubleRoomPrice() < 0) {
            return "Los precios de las habitaciones no pueden ser negativos";
        }
        if (hotelDto.getSingleRoomsQ() < 0 || hotelDto.getDoubleRoomsQ() < 0) {
            return "El número de habitaciones tiene que ser mayor que 0";
        }
        hotelRepository.save(hotelMapper.dtoToEntity(hotelDto));
        return "El hotel ha sido añadido con éxito";
    }

    /**
     * Elimina un hotel de forma lógica (lo marca como inactivo) siempre que no tenga
     * habitaciones con reservas activas.
     *
     * @param id Identificador del hotel a eliminar.
     * @return Un mensaje indicando el resultado de la eliminación.
     */
    @Override
    @Transactional
    public String deleteHotel(Long id) {
        Hotel hotel = findHotelById(id);
        if (hotel == null || !hotel.isActive()) {
            return "El hotel no ha sido eliminado porque no ha sido encontrado";
        }
        hotel.setActive(false);
        hotelRepository.save(hotel);
        return "El hotel ha sido eliminado con éxito por lógica";
    }

    /**
     * Edita la información de un hotel existente.
     *
     * @param id       Identificador del hotel a editar.
     * @param hotelDto DTO que contiene la información actualizada del hotel.
     * @return Un mensaje indicando el resultado de la operación.
     */
    @Override
    @Transactional
    public String editHotel(Long id, HotelDto hotelDto) {
        Hotel hotelAnt = findHotelById(id);
        if (hotelAnt == null || !hotelAnt.isActive()) {
            return "No existe ningún hotel con ese id";
        }
        if (hotelDto.getHotelCode().isEmpty() || hotelDto.getName().isEmpty() || hotelDto.getPlace().isEmpty()) {
            return "No se pueden añadir campos vacíos";
        }
        // Validaciones adicionales (opcional)
        if (hotelDto.getSimpleRoomPrice() < 0 || hotelDto.getDoubleRoomPrice() < 0) {
            return "Los precios de las habitaciones no pueden ser negativos";
        }
        if (hotelDto.getSingleRoomsQ() < 0 || hotelDto.getDoubleRoomsQ() < 0) {
            return "El número de habitaciones tiene que ser mayor que 0";
        }
        hotelAnt.setHotelCode(hotelDto.getHotelCode());
        hotelAnt.setName(hotelDto.getName());
        hotelAnt.setPlace(hotelDto.getPlace());
        hotelAnt.setSingleRoomsQ(hotelDto.getSingleRoomsQ());
        hotelAnt.setDoubleRoomsQ(hotelDto.getDoubleRoomsQ());
        hotelAnt.setDoubleRoomPrice(hotelDto.getDoubleRoomPrice());
        hotelAnt.setSimpleRoomPrice(hotelDto.getSimpleRoomPrice());
        try {
            hotelRepository.save(hotelAnt);
            return "El hotel ha sido editado con éxito";
        } catch (DataIntegrityViolationException e) {
            return "Ocurrió un error al editar el hotel: " + e.getMessage();
        }
    }

    /**
     * Decrementa la disponibilidad de habitaciones en un hotel.
     * Se restan las habitaciones dobles y simples indicadas.
     *
     * @param hotel       Hotel a actualizar.
     * @param doubleRooms Número de habitaciones dobles a restar.
     * @param singleRooms Número de habitaciones simples a restar.
     */
    @Transactional
    public void decrementRoomAvailability(Hotel hotel, int doubleRooms, int singleRooms) {
        hotel.setDoubleRoomsQ(hotel.getDoubleRoomsQ() - doubleRooms);
        hotel.setSingleRoomsQ(hotel.getSingleRoomsQ() - singleRooms);
        hotelRepository.save(hotel);
    }

    /**
     * Incrementa la disponibilidad de habitaciones en un hotel.
     * Se suman las habitaciones dobles y simples indicadas.
     *
     * @param hotel       Hotel a actualizar.
     * @param doubleRooms Número de habitaciones dobles a sumar.
     * @param singleRooms Número de habitaciones simples a sumar.
     */
    @Transactional
    public void incrementRoomAvailability(Hotel hotel, int doubleRooms, int singleRooms) {
        hotel.setDoubleRoomsQ(hotel.getDoubleRoomsQ() + doubleRooms);
        hotel.setSingleRoomsQ(hotel.getSingleRoomsQ() + singleRooms);
        hotelRepository.save(hotel);
    }

    /**
     * Retorna una lista con todos los hoteles existentes en la base de datos, en formato DTO.
     *
     * @return Lista de {@link HotelDto} con la información de los hoteles.
     */
    @Override
    @Transactional
    public List<HotelDto> listHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::entityToDto)
                .toList();
    }
}




