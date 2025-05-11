package org.example.eventbookingsystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.Service.BookingService;
import org.example.eventbookingsystem.dto.BookingRequestDTO;
import org.example.eventbookingsystem.dto.BookingResponseDTO;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO, Authentication authentication) {
        log.info("Creating the booking with eventId: {} and capacity: {}", bookingRequestDTO.getEventId(), bookingRequestDTO.getCapacity());
        User user = (User) authentication.getPrincipal();
       return ResponseEntity.ok().body(bookingService.create(bookingRequestDTO, user));
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBooking(@PathVariable Long id) {
        log.info("Returnning the booking with id: {}", id);
        return bookingService.getBooking(id);
    }
}
