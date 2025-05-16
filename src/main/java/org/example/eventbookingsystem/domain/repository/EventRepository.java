package org.example.eventbookingsystem.domain.repository;


import org.example.eventbookingsystem.domain.entity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
