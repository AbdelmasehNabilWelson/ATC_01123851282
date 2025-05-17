package org.example.eventbookingsystem.common.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.dto.CreateEventRequestDTO;
import org.example.eventbookingsystem.api.dto.EventCriteriaFilter;
import org.example.eventbookingsystem.api.dto.EventResponseDTO;
import org.example.eventbookingsystem.api.dto.UpdateEventDTO;
import org.example.eventbookingsystem.domain.entity.Category;
import org.example.eventbookingsystem.domain.entity.Event;
import org.example.eventbookingsystem.api.advice.EventNotFoundException;
import org.example.eventbookingsystem.common.repository.EventRepository;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.example.eventbookingsystem.specification.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository  userRepository;
    private final AzureBlobStorageService azureBlobStorageService;
    public EventService(EventRepository eventRepository, UserRepository userRepository, AzureBlobStorageService azureBlobStorageService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.azureBlobStorageService = azureBlobStorageService;
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

    public Page<EventResponseDTO> getEventsWithCriteria(EventCriteriaFilter eventCriteriaFilter, Pageable pageable) {
        Specification<Event> eventSpecification =  Specification.where(null);
        eventSpecification = eventSpecification.and(EventSpecification.hasCategoryId(eventCriteriaFilter.getCategoryId()));
        eventSpecification = eventSpecification.and(EventSpecification.hasCategoryName(eventCriteriaFilter.getCategoryName()));
        eventSpecification = eventSpecification.and(EventSpecification.withinTimeRange(eventCriteriaFilter.getStartTime(), eventCriteriaFilter.getEndTime()));
        eventSpecification = eventSpecification.and(EventSpecification.hasState(eventCriteriaFilter.getEventState(), OffsetDateTime.now()));

        Page<Event> events = eventRepository.findAll(eventSpecification, pageable);
        return events.map(this::convertEventToEventResponseDTO);
    }

    public String uploadEventImage(Long id, MultipartFile image) throws IOException {
        log.info("Uploading event image for the event with id: {}", id);

        if (image == null || image.isEmpty()) {
            log.error("Image is null or Empty");
            throw new IllegalArgumentException("Image is empty");
        }

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        String imageURL = "";
        try {
            imageURL = azureBlobStorageService.storeImage(image);
        } catch (IOException e) {
            log.error("Error while uploading image: {}", e.getMessage());
            throw new IOException("Error while uploading image");
        }

        event.setImageUrl(imageURL);
        eventRepository.save(event);
        log.info("Event image uploaded successfully for the event with id: {}", id);
        return imageURL;
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
        createEventResponseDTO.setCategoryName(
                event.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
        );
        createEventResponseDTO.setEndTime(event.getEndTime());
        createEventResponseDTO.setStartTime(event.getStartTime());
        return createEventResponseDTO;
    }

}
