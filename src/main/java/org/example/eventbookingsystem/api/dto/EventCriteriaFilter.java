package org.example.eventbookingsystem.api.dto;

import lombok.Data;
import org.example.eventbookingsystem.domain.entity.Event;

import java.time.OffsetDateTime;

@Data
public class EventCriteriaFilter {
    private String categoryName;
    private Long categoryId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Event.EVENT_STATE eventState;
}
