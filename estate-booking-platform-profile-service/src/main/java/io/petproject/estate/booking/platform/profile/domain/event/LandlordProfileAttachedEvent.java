package io.petproject.estate.booking.platform.profile.domain.event;

import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

import java.time.Instant;
import java.util.UUID;

public record LandlordProfileAttachedEvent(
    UUID eventId,
    UUID aggregateId,
    Instant occurredAt
) implements DomainEvent {
    private static final String AGGREGATE_TYPE = "USER_PROFILE";
    private static final String EVENT_TYPE = "LANDLORD_PROFILE_ATTACHED";

    public static LandlordProfileAttachedEvent of(
        UserId userId,
        Instant occurredAt
    ) {
        return new LandlordProfileAttachedEvent(
            UUID.randomUUID(),
            userId.value(),
            occurredAt
        );
    }

    @Override
    public String aggregateType() {
        return AGGREGATE_TYPE;
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
