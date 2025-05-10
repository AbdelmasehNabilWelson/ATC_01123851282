package org.example.eventbookingsystem.entity;

import jakarta.persistence.*;
import org.example.eventbookingsystem.security.User;

import java.time.Instant;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
