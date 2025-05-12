package org.example.eventbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.eventbookingsystem.security.Entity.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
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

    @ManyToMany(mappedBy = "events")
    private Set<Category> categories = new HashSet<>();
}
