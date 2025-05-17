package org.example.eventbookingsystem.api.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.common.Service.BookingService;
import org.example.eventbookingsystem.api.dto.BookingRequestDTO;
import org.example.eventbookingsystem.api.dto.BookingResponseDTO;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO, Authentication authentication) {
        log.info("Creating the booking with eventId: {} and capacity: {}", bookingRequestDTO.getEventId(), bookingRequestDTO.getCapacity());
        User user = (User) authentication.getPrincipal();
       return ResponseEntity.ok().body(bookingService.create(bookingRequestDTO, user));
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBooking(@PathVariable Long id) {
        log.info("Returnning the booking with id: {}", id);
        return bookingService.getBooking(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getAll(user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO bookingUpdateRequestDTO) {
        log.info("Updating the booking with id: {}", id);
        return ResponseEntity.ok(bookingService.update(id, bookingUpdateRequestDTO));
    }
}
