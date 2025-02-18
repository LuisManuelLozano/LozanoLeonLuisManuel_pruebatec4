package com.luis.agencia.repository;

import com.luis.agencia.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Hotel}.
 * Define las operaciones de acceso a datos para los hoteles, incluyendo una consulta
 * para obtener un hotel basado en su código único.
 */
public interface IHotelRepository extends JpaRepository<Hotel, Long> {

    /**
     * Busca un hotel por su código único.
     *
     * @param hotelCode el código del hotel.
     * @return un Optional que contiene el hotel encontrado o vacío si no existe.
     */
    Optional<Hotel> findByHotelCode(String hotelCode);
}

