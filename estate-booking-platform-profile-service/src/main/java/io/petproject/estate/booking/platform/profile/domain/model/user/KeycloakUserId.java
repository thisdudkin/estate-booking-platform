package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.UUID;

public record KeycloakUserId(UUID value) {
    public KeycloakUserId {
        DomainAssertions.requireNonNull(value, "keycloakUserId");
    }
}
