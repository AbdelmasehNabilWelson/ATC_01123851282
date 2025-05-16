package org.example.eventbookingsystem.api.dto;

import jakarta.persistence.Embedded;
import lombok.Data;
import org.example.eventbookingsystem.domain.entity.entity.Address;

@Data
public class EventResponseDTO {
    private Long id;

    private String title;
    @Embedded
    private Address address;

    private String description;
    private Long capacity;
}
