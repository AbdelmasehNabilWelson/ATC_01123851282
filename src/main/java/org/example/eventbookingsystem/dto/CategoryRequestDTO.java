package org.example.eventbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;
}
