package org.example.eventbookingsystem.api.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.advice.EventNotFoundException;
import org.example.eventbookingsystem.api.dto.EventCriteriaFilter;
import org.example.eventbookingsystem.common.Service.EventService;
import org.example.eventbookingsystem.api.dto.CreateEventRequestDTO;
import org.example.eventbookingsystem.api.dto.EventResponseDTO;
import org.example.eventbookingsystem.api.dto.UpdateEventDTO;
import org.example.eventbookingsystem.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;


@Slf4j
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


    @GetMapping("/allEvents")
    public ResponseEntity<Page<EventResponseDTO>> getAllEvents(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime,
            @RequestParam(required = false) Event.EVENT_STATE eventState,

            @PageableDefault(size = 20, sort = {"startTime"}, direction = Sort.Direction.ASC)
            Pageable pageable) {


        log.info("Calling getting all events with criteria and pageable");

        EventCriteriaFilter eventCriteriaFilter = new EventCriteriaFilter();
        eventCriteriaFilter.setCategoryName(categoryName);
        eventCriteriaFilter.setEventState(eventState);
        eventCriteriaFilter.setCategoryId(categoryId);
        eventCriteriaFilter.setStartTime(startTime);
        eventCriteriaFilter.setEndTime(endTime);
        eventCriteriaFilter.setEventState(eventState);

        Page<EventResponseDTO> events = eventService.getEventsWithCriteria(eventCriteriaFilter, pageable);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{id}/uploadEventImage")
    public ResponseEntity<String> uploadEventImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        log.info("Uploading event image for the event with id: {}", id);
        String ImageURL;
        try {
            ImageURL = eventService.uploadEventImage(id, image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the event image: " + e.getMessage());
        } catch (IllegalArgumentException | EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to upload the event image: " + e.getMessage());
        }
        return ResponseEntity.ok("Event image saved: "+  ImageURL);
    }
}
