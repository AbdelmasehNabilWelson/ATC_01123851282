package org.example.eventbookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @Size(min = 5, max = 50)
    private String username;

    @Email
    private String email;

    @Size(min = 8, max = 20)
    private String password;
}
