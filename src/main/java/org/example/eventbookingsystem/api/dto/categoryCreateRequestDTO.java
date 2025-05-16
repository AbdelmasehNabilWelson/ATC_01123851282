package org.example.eventbookingsystem.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class categoryCreateRequestDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;
}
