package org.example.eventbookingsystem.api.dto;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eventbookingsystem.domain.entity.Address;

import java.time.OffsetDateTime;

@Data
public class CreateEventRequestDTO {
    @Size(min = 5, max = 50)
    @NotNull
    private String title;

    @Size(min = 10, max = 500)
    private String description;

    @Embedded
    private Address address;

    @NotNull
    private Long capacity;

    @NotNull
    private OffsetDateTime startTime;
    @NotNull
    private OffsetDateTime endTime;
}
