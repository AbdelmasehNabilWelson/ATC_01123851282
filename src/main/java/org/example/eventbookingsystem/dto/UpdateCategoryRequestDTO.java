package org.example.eventbookingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;
}
