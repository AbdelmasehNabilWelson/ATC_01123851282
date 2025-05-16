package org.example.eventbookingsystem.domain.repository;

import org.example.eventbookingsystem.domain.entity.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
