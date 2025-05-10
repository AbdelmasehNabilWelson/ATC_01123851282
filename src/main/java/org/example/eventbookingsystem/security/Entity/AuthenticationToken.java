package org.example.eventbookingsystem.security.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class AuthenticationToken {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
