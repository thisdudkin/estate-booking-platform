package io.petproject.estate.booking.platform.profile.domain.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();

    String aggregateType();

    UUID aggregateId();

    String eventType();

    Instant occurredAt();
}
