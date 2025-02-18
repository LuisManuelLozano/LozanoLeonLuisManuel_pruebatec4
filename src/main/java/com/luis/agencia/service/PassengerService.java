package com.luis.agencia.service;

import com.luis.agencia.dto.PassengerDto;
import com.luis.agencia.mapper.PassengerMapper;
import com.luis.agencia.model.Passenger;
import com.luis.agencia.repository.IPassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica relacionada con los pasajeros,
 * incluyendo su creación, edición, eliminación y obtención de datos en formato DTO.
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // El IDE no detecta la inyección en tiempo de compilación
public class PassengerService implements IPassengerService {

    /**
     * Repositorio para acceder a la información de los pasajeros en la base de datos.
     * Inyectado por Spring.
     */
    @Autowired
    private IPassengerRepository passengerRepository;

    /**
     * Mapper para convertir entre entidades {@link Passenger} y DTOs {@link PassengerDto}.
     * Inyectado por Spring.
     */
    @Autowired
    private PassengerMapper passengerMapper;

    /**
     * Busca un pasajero por su identificador y devuelve la entidad.
     *
     * @param id Identificador del pasajero.
     * @return El pasajero si se encuentra, o null en caso contrario.
     */
    @Override
    @Transactional
    public Passenger findPassengerById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Passenger> passengerOpt = passengerRepository.findById(id);
        return passengerOpt.orElse(null);
    }

    /**
     * Busca un pasajero por su identificador y lo devuelve en formato DTO.
     *
     * @param id Identificador del pasajero.
     * @return El DTO del pasajero si se encuentra, o null en caso contrario.
     */
    @Override
    @Transactional
    public PassengerDto findPassengerDtoById(Long id) {
        Passenger passenger = findPassengerById(id);
        if (passenger == null) {
            return null;
        }
        return passengerMapper.entityToDto(passenger);
    }

    /**
     * Retorna la lista de todos los pasajeros en formato DTO.
     *
     * @return Lista de DTOs de todos los pasajeros.
     */
    @Override
    @Transactional
    public List<PassengerDto> listPassengers() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::entityToDto)
                .toList();
    }

    /**
     * Crea un nuevo pasajero en la base de datos a partir de un DTO,
     * verificando que no exista un pasajero con el mismo DNI.
     *
     * @param passengerDto DTO con la información del pasajero.
     * @return El DTO del pasajero creado.
     * @throws IllegalArgumentException si el pasajero es nulo o el DNI ya existe.
     */
    @Override
    @Transactional
    public PassengerDto createPassenger(PassengerDto passengerDto) {
        if (passengerDto == null) {
            throw new IllegalArgumentException("El pasajero no puede ser nulo");
        }
        // Verificar duplicidad por DNI
        Optional<Passenger> existing = passengerRepository.findByDni(passengerDto.getDni());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un pasajero con el DNI " + passengerDto.getDni());
        }
        Passenger passenger = passengerMapper.dtoToEntity(passengerDto);
        passenger = passengerRepository.save(passenger);
        return passengerMapper.entityToDto(passenger);
    }

    /**
     * Actualiza un pasajero existente a partir de un DTO.
     *
     * @param id           Identificador del pasajero a actualizar.
     * @param passengerDto DTO con la información nueva del pasajero.
     * @return El DTO del pasajero actualizado.
     * @throws IllegalArgumentException si el pasajero no existe.
     */
    @Override
    @Transactional
    public PassengerDto updatePassenger(Long id, PassengerDto passengerDto) {
        Passenger existing = findPassengerById(id);
        if (existing == null) {
            throw new IllegalArgumentException("El pasajero no existe");
        }
        // Si se actualiza el DNI, podría verificarse duplicidad aquí también (opcional).
        existing.setName(passengerDto.getName());
        existing.setLastName(passengerDto.getLastName());
        existing.setDni(passengerDto.getDni());
        Passenger updated = passengerRepository.save(existing);
        return passengerMapper.entityToDto(updated);
    }

    /**
     * Elimina físicamente un pasajero de la base de datos.
     *
     * @param id Identificador del pasajero a eliminar.
     * @throws IllegalArgumentException si el pasajero no existe.
     */
    @Override
    @Transactional
    public void deletePassenger(Long id) {
        Passenger existing = findPassengerById(id);
        if (existing == null) {
            throw new IllegalArgumentException("El pasajero no existe");
        }
        passengerRepository.deleteById(id);
    }

    /**
     * Verifica si existe un pasajero con el identificador proporcionado.
     *
     * @param id Identificador del pasajero.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existsById(Long id) {
        return passengerRepository.existsById(id);
    }

    /**
     * Guarda o actualiza un pasajero en la base de datos.
     *
     * @param passenger Entidad del pasajero a guardar.
     */
    @Override
    public void save(Passenger passenger) {
        passengerRepository.save(passenger);
    }
}





