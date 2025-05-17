package org.example.eventbookingsystem.common.repository;

import org.example.eventbookingsystem.domain.entity.Booking;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByUserId(Long userId);

    Long user(User user);
}
