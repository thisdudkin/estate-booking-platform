package io.petproject.estate.booking.platform.profile.domain.event;

import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserProfileStatusChangedEvent(
    UUID eventId,
    UUID aggregateId,
    String oldStatus,
    String newStatus,
    Instant occurredAt
) implements DomainEvent {
    private static final String AGGREGATE_TYPE = "USER_PROFILE";
    private static final String EVENT_TYPE = "USER_PROFILE_STATUS_CHANGED";

    public static UserProfileStatusChangedEvent of(
        UserId userId,
        UserStatus oldStatus,
        UserStatus newStatus,
        Instant occurredAt
    ) {
        return new UserProfileStatusChangedEvent(
            UUID.randomUUID(),
            userId.value(),
            oldStatus.name(),
            newStatus.name(),
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
