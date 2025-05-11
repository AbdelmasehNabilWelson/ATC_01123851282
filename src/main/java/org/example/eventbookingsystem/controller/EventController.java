package org.example.eventbookingsystem.controller;

import jakarta.validation.Valid;
import org.example.eventbookingsystem.Service.EventService;
import org.example.eventbookingsystem.dto.CreateEventRequestDTO;
import org.example.eventbookingsystem.dto.EventResponseDTO;
import org.example.eventbookingsystem.dto.UpdateEventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@PreAuthorize("hasRole('ADMIN')")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping("/create")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid  @RequestBody CreateEventRequestDTO createEventDTO, Authentication authentication) {
        String loggedInAdminUsername = authentication.getName();
        return ResponseEntity.ok(eventService.createEvent(createEventDTO, loggedInAdminUsername));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody UpdateEventDTO updateEventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, updateEventDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}
