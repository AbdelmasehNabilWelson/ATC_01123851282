package org.example.eventbookingsystem.api.dto;

import jakarta.annotation.security.DenyAll;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileDTO {
    @NotBlank(message = "User name should not be null")
    @Size(min = 5, max = 50, message = "User name should be between 5 and 50 characters")
    private String username;

    @NotBlank(message = "Email should not be null")
    @Email(message = "provide a valid email")
    private String email;

    @NotBlank(message = "Password should not be null")
    private String oldPassword;

    @Size(min = 6, max = 20, message = "New Password should be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character. special chars are [@#$%^&+=]")
    private String newPassword;

    @Size(min = 6, max = 20, message = "New Password should be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character. special chars are [@#$%^&+=]")
    private String confirmNewPassword;
}
