package com.luis.agencia.repository;

import com.luis.agencia.model.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link RoomBooking}.
 * Extiende de JpaRepository para proporcionar las operaciones CRUD básicas
 * y consultas adicionales sobre la base de datos.
 */
public interface IRoomBookingRepository extends JpaRepository<RoomBooking, Long> {
}

