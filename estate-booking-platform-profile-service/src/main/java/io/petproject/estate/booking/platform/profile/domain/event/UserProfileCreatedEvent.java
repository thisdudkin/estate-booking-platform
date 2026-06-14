package io.petproject.estate.booking.platform.profile.domain.event;

import io.petproject.estate.booking.platform.profile.domain.model.user.EmailAddress;
import io.petproject.estate.booking.platform.profile.domain.model.user.KeycloakUserId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

import java.time.Instant;
import java.util.UUID;

public record UserProfileCreatedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID keycloakUserId,
    String email,
    Instant occurredAt
) implements DomainEvent {
    private static final String AGGREGATE_TYPE = "USER_PROFILE";
    private static final String EVENT_TYPE = "USER_PROFILE_CREATED";

    public static UserProfileCreatedEvent of(
        UserId userId,
        KeycloakUserId keycloakUserId,
        EmailAddress email,
        Instant occurredAt
    ) {
        return new UserProfileCreatedEvent(
            UUID.randomUUID(),
            userId.value(),
            keycloakUserId.value(),
            email.value(),
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
