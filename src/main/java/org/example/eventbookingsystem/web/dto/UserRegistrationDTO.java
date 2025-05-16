package org.example.eventbookingsystem.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "User name should not be null")
    @Size(min = 5, max = 50, message = "User name should be between 5 and 50 characters")
    private String username;

    @NotBlank(message = "Email should not be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should not be null")
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character. special chars are [@#$%^&+=]")
    private String password;

    @NotBlank(message = "Confirm Password should not be null")
    private String confirmPassword;
}
