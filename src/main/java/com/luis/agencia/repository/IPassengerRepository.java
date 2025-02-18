package com.luis.agencia.repository;

import com.luis.agencia.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Passenger}.
 * Define operaciones de acceso a datos, además de consultas personalizadas para obtener
 * pasajeros en función de parámetros específicos.
 */
public interface IPassengerRepository extends JpaRepository<Passenger, Long> {

    /**
     * Busca un pasajero por su número de DNI.
     *
     * @param dni el número de identificación del pasajero, no puede ser nulo.
     * @return un Optional que contiene el pasajero encontrado o vacío si no existe.
     */
    Optional<Passenger> findByDni(@NonNull String dni);

    /**
     * Verifica si existe un pasajero con el identificador proporcionado.
     *
     * @param id el identificador del pasajero, no puede ser nulo.
     * @return true si existe un pasajero con ese ID, false en caso contrario.
     */
    boolean existsById(@NonNull Long id);
}


