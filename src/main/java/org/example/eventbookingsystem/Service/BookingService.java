package org.example.eventbookingsystem.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.dto.BookingRequestDTO;
import org.example.eventbookingsystem.dto.BookingResponseDTO;
import org.example.eventbookingsystem.entity.Booking;
import org.example.eventbookingsystem.entity.Event;
import org.example.eventbookingsystem.repository.BookingRepository;
import org.example.eventbookingsystem.repository.EventRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;

    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
    }

    public BookingResponseDTO create(BookingRequestDTO bookingRequestDTO, User user) {
        Event event = eventRepository.findById(bookingRequestDTO.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event with id: " + bookingRequestDTO.getEventId() + " not found"));

        if (event.getCapacity() < bookingRequestDTO.getCapacity()) {
            throw new IllegalArgumentException("Event capacity must be in less than or equal " + event.getCapacity());
        }

        event.setCapacity(event.getCapacity() - bookingRequestDTO.getCapacity());
        Booking booking = new Booking();
        booking.setCreatedAt(Instant.now());
        booking.setUser(user);
        booking.setEvent(event);
        booking.setCapacity(bookingRequestDTO.getCapacity());
        bookingRepository.save(booking);

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(booking.getId());
        return responseDTO;
    }

    public BookingResponseDTO getBooking(Long id) {
        log.info("Returnning the booking with id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + id + " not found"));

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(booking.getId());
        return responseDTO;
    }
}
