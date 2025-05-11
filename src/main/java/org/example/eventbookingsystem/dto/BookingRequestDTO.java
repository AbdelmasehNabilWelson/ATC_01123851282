package org.example.eventbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDTO {
    @NotNull
    private Long eventId;
    @NotNull
    private Long capacity;
}