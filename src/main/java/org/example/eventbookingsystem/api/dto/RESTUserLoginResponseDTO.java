package org.example.eventbookingsystem.api.dto;




import lombok.Data;

@Data
public class RESTUserLoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
}
