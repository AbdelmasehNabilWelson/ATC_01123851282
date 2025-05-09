package org.example.eventbookingsystem.dto;




import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
}
