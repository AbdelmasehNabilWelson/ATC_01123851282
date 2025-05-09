package org.example.eventbookingsystem.entity;

import jakarta.persistence.*;
import org.example.eventbookingsystem.security.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;
    private String description;

    @Embedded
    private Address address;

    private Long capacity;

    private Instant createdAt;
    private Instant updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
