package org.example.eventbookingsystem.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.security.Entity.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
@ToString(exclude = {"categories", "eventBookings"})
@EqualsAndHashCode(exclude = {"categories", "eventBookings"})
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

    private String imageUrl;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;


    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "event")
    private Set<Booking> eventBookings = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    private Set<Category> categories = new HashSet<>();

    @PrePersist
    private void prePersist() {
        Instant currentUniversalPointInTime = Instant.now();
        this.createdAt = currentUniversalPointInTime;
        this.updatedAt = currentUniversalPointInTime;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public enum EVENT_STATE {
        UPCOMING,
        INGOING,
        FINISHED,
        INVALID_DATES
    }

    @Transient
    public EVENT_STATE getEventStatus() {
        if (startTime == null || endTime == null) {
            log.error("Event start or end time is null");
            return EVENT_STATE.INVALID_DATES;
        }

        if (endTime.isBefore(startTime)) {
            log.error("Start must be before end time");
            return EVENT_STATE.INVALID_DATES;
        }

        Instant currentTime = Instant.now();

        Instant start = startTime.toInstant();
        Instant end = endTime.toInstant();

        if (currentTime.isBefore(start)) {
            log.info("Event is upcoming");
            return EVENT_STATE.UPCOMING;
        } else if (currentTime.isAfter(start) && currentTime.isBefore(end)) {
            log.info("Event is ongoing");
            return EVENT_STATE.INGOING;
        } else {
            log.info("Event is finished");
            return EVENT_STATE.FINISHED;
        }
    }
}
