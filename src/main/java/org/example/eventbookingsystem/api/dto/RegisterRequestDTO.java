package org.example.eventbookingsystem.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @Size(min = 5, max = 50)
    private String username;

    @Email
    private String email;

    @Size(min = 6, max = 20)
    private String password;
}
