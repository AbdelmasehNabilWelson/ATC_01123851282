package org.example.eventbookingsystem.api.dto;




import lombok.Data;

@Data
public class JwtLogInResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
}
