package org.example.eventbookingsystem.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.example.eventbookingsystem.domain.entity.Category;
import org.example.eventbookingsystem.domain.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class EventSpecification {

    public static Specification<Event> hasCategoryName(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return cb.conjunction(); // necessary when combining specifications
            }

            Join<Event, Category> categoryJoin = root.join("categories");

            return cb.equal(cb.lower(categoryJoin.get("name")), categoryName.toLowerCase());
        };
    }

    public static Specification<Event> hasCategoryId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction(); // evaluates to true. helpful when combined with other specifications
            }

            Join<Event, Category> categoryJoin = root.join("categories");
            return cb.equal(categoryJoin.get("id"), id);
        };
    }

    public static Specification<Event> startAfter(OffsetDateTime time) {
        return (root, query, cb) -> {
            if (time == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("startTime"), time);
        };
    }

    public static Specification<Event> startBefore(OffsetDateTime time) {
        return (root, query, cb) -> {
            if (time == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("startTime"), time);
        };
    }

    public static Specification<Event> withinTimeRange(OffsetDateTime startTime, OffsetDateTime endTime) {
        return (root, query, cb) -> {
            if (startTime != null && endTime != null) {
                Predicate eventStartsBeforeOrAtQueryEnd = cb.lessThanOrEqualTo(root.get("startTime"), endTime);
                Predicate eventEndAfterOrAtQueryStartTime = cb.greaterThanOrEqualTo(root.get("endTime"), startTime);

                return cb.and(eventStartsBeforeOrAtQueryEnd, eventEndAfterOrAtQueryStartTime);
            } else if (startTime != null) {
                return cb.greaterThanOrEqualTo(root.get("endTime"), startTime);
            } else if (endTime != null) {
                return cb.lessThanOrEqualTo(root.get("startTime"), endTime);
            }
            
            return cb.conjunction();
        };
    }

    public static Specification<Event> hasState(Event.EVENT_STATE state, OffsetDateTime now) {
        if (state == null || now == null) {
            return (r, q, cb) -> cb.conjunction();
        }

        return (root, query, cb) -> {
            Path<OffsetDateTime> startTimePath = root.get("startTime");
            Path<OffsetDateTime> endTimePath = root.get("endTime");

            Predicate validDates = cb.lessThanOrEqualTo(startTimePath, endTimePath);

            switch (state) {
                case UPCOMING -> {
                    Predicate eventStartInFuture = cb.greaterThan(startTimePath, now);
                    return cb.and(validDates, eventStartInFuture);
                }
                case INGOING -> {
                    Predicate eventHasStarted = cb.lessThanOrEqualTo(startTimePath, now);
                    Predicate eventNotEnded = cb.greaterThanOrEqualTo(endTimePath, now);
                    return cb.and(validDates, eventHasStarted, eventNotEnded);
                }
                case FINISHED -> {
                    Predicate eventHasEnded = cb.lessThan(endTimePath, now);
                    return cb.and(validDates, eventHasEnded);
                }
                case INVALID_DATES -> {
                    return cb.lessThan(endTimePath, startTimePath);
                }
                default -> {
                    return cb.conjunction();
                }
            }
        };
    }

}
