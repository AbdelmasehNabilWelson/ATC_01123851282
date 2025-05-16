package org.example.eventbookingsystem.domain.entity.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.eventbookingsystem.security.Entity.User;

import java.time.Instant;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private Instant createdAt;

    private Long capacity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
