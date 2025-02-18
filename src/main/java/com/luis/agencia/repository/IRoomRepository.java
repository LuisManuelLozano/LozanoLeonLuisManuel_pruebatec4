package com.luis.agencia.repository;

import com.luis.agencia.model.Room;
import com.luis.agencia.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IRoomRepository extends JpaRepository<Room, Long> {

    /**
     * Devuelve todas las habitaciones de un hotel por su ID.
     */
    List<Room> findByHotelId(Long hotelId);

    /**
     * Devuelve habitaciones disponibles (sin reserva) de un tipo específico dentro de un rango de fechas.
     */
    List<Room> findByRoomTypeAndDisponibilityDateFromLessThanEqualAndDisponibilityDateToGreaterThanEqualAndRoomBookingIsNull(
            RoomType roomType,
            LocalDate dateFrom,
            LocalDate dateTo
    );

    /**
     * Devuelve habitaciones disponibles por tipo y destino (usando JPQL).
     * Filtra por roomType, rango de fechas y que el hotel esté en el destino indicado.
     */
    @Query("SELECT r FROM Room r " +
            "WHERE r.roomType = :roomType " +
            "AND r.disponibilityDateFrom <= :dateTo " +
            "AND r.disponibilityDateTo >= :dateFrom " +
            "AND r.roomBooking IS NULL " +
            "AND LOWER(r.hotel.place) = LOWER(:destination)")
    List<Room> findAvailableRoomsByTypeAndDestination(
            @Param("roomType") RoomType roomType,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("destination") String destination
    );

    /**
     * Devuelve habitaciones (de un hotel específico) con disponibilidad en un rango de fechas.
     */
    List<Room> findByHotelIdAndDisponibilityDateFromLessThanEqualAndDisponibilityDateToGreaterThanEqual(
            Long hotelId,
            LocalDate disponibilityDateFrom,
            LocalDate disponibilityDateTo
    );

    /**
     * Devuelve habitaciones disponibles por destino y rango de fechas (usando JPQL).
     */
    @Query("SELECT r FROM Room r " +
            "WHERE LOWER(r.hotel.place) = LOWER(:destination) " +
            "AND r.disponibilityDateFrom <= :toDate " +
            "AND r.disponibilityDateTo >= :fromDate " +
            "AND r.roomBooking IS NULL")
    List<Room> findAvailableRoomsByDestination(
            @Param("destination") String destination,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}






