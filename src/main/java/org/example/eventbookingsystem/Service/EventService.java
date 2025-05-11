package org.example.eventbookingsystem.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.dto.CreateEventRequestDTO;
import org.example.eventbookingsystem.dto.EventResponseDTO;
import org.example.eventbookingsystem.dto.UpdateEventDTO;
import org.example.eventbookingsystem.entity.Event;
import org.example.eventbookingsystem.exceptions.EventNotFoundException;
import org.example.eventbookingsystem.repository.EventRepository;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository  userRepository;


    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    public EventResponseDTO createEvent(CreateEventRequestDTO createEventDTO, String username) {
        log.info("Creating the event with title: {} by the Admin {}", createEventDTO.getTitle(), username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin with username: " + username + "not found"));

        Event event = convertCreateEventDTOToEvent(createEventDTO);
        event.setUser(user);



        eventRepository.save(event);
        log.info("Event created successfully with Address: {}, and title {}", event.getAddress(), event.getTitle());

        return convertEventToEventResponseDTO(event);
    }

    public EventResponseDTO getEvent(Long id) {
        log.info("Getting the event with id: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        return convertEventToEventResponseDTO(event);
    }

    public void deleteEvent(Long id) {
        log.info("Deleting the event with id: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        eventRepository.delete(event);
        log.info("Event deleted successfully with id: {}", id);
    }

    public EventResponseDTO updateEvent(Long id, UpdateEventDTO updateEventDTO) {
        log.info("Updating the event: {}", updateEventDTO);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        event.setTitle(updateEventDTO.getTitle());
        event.setDescription(updateEventDTO.getDescription());
        event.setAddress(updateEventDTO.getAddress());
        event.setCapacity(updateEventDTO.getCapacity());

        eventRepository.save(event);
        log.info("Event updated successfully: {}", event);

        return convertEventToEventResponseDTO(event);
    }

    public List<EventResponseDTO> getAllEvents() {
        log.info("Returning all events");
        var allEvents = eventRepository.findAll();

        List<EventResponseDTO> responseDTOList = allEvents.stream()
                .map(this::convertEventToEventResponseDTO)
                .toList();
        return responseDTOList;
    }

    private Event convertCreateEventDTOToEvent(CreateEventRequestDTO createEventDTO) {
        Event event = new Event();
        event.setTitle(createEventDTO.getTitle());
        event.setDescription(createEventDTO.getDescription());
        event.setAddress(createEventDTO.getAddress());
        event.setCapacity(createEventDTO.getCapacity());
        event.setCreatedAt(Instant.now());
        return event;
    }

    private EventResponseDTO convertEventToEventResponseDTO(Event event) {
        EventResponseDTO createEventResponseDTO = new EventResponseDTO();
        createEventResponseDTO.setId(event.getId());
        createEventResponseDTO.setTitle(event.getTitle());
        createEventResponseDTO.setAddress(event.getAddress());
        createEventResponseDTO.setDescription(event.getDescription());
        createEventResponseDTO.setCapacity(event.getCapacity());
        return createEventResponseDTO;
    }

}
