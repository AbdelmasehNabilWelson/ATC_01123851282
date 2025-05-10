package org.example.eventbookingsystem.entity;

import jakarta.persistence.*;
import org.example.eventbookingsystem.security.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "event")
    private Set<Booking> eventBookings = new HashSet<>();
}
