package com.luis.agencia.repository;

import com.luis.agencia.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFlightBookingRepository extends JpaRepository<FlightBooking, Long> {
}
