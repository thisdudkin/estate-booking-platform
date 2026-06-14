package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        DomainAssertions.requireNonNull(value, "userId");
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}
