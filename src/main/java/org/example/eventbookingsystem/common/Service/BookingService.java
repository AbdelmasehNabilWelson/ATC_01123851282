package org.example.eventbookingsystem.common.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.dto.BookingRequestDTO;
import org.example.eventbookingsystem.api.dto.BookingResponseDTO;
import org.example.eventbookingsystem.domain.entity.Booking;
import org.example.eventbookingsystem.domain.entity.Event;
import org.example.eventbookingsystem.common.repository.BookingRepository;
import org.example.eventbookingsystem.common.repository.EventRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
        responseDTO.setCapacity(booking.getCapacity());
        return responseDTO;
    }

    public BookingResponseDTO getBooking(Long id) {
        log.info("Returnning the booking with id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + id + " not found"));

       return convertBookingToBookingResponseDTO(booking);
    }

    public BookingResponseDTO update(Long id, BookingRequestDTO bookingRequestDTO) {
        log.info("Updating the booking with id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + id + " not found"));

        Event event = booking.getEvent();
        Long existingCapacity = event.getCapacity();
        Long oldCapacity = booking.getCapacity();
        Long newCapacity = bookingRequestDTO.getCapacity();

        if (newCapacity > existingCapacity + oldCapacity) {
            throw new EntityNotFoundException("Event capacity must be less than or equal to " + (existingCapacity + oldCapacity));
        }
        event.setCapacity(existingCapacity + oldCapacity - newCapacity);
        booking.setCapacity(newCapacity);
        bookingRepository.save(booking);

        return convertBookingToBookingResponseDTO(booking);
    }

    public List<BookingResponseDTO> getAll(Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(userId);
        List<BookingResponseDTO> bookingResponse =
                bookings.stream().map(this::convertBookingToBookingResponseDTO).collect(Collectors.toList());
        return bookingResponse;
    }

    private BookingResponseDTO convertBookingToBookingResponseDTO(Booking booking) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setId(booking.getId());
        bookingResponseDTO.setCapacity(booking.getCapacity());
        return bookingResponseDTO;
    }
}
