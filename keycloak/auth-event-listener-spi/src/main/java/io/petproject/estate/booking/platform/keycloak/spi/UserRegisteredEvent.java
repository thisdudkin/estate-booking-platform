package io.petproject.estate.booking.platform.keycloak.spi;

import java.io.Serializable;
import java.time.Instant;

record UserRegisteredEvent(
        String eventId,
        String eventType,
        Instant occurredAt,
        String producer,
        UserRegisteredPayload payload
) implements Serializable { }
