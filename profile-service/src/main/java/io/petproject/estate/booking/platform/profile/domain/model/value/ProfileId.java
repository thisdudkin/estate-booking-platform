package io.petproject.estate.booking.platform.profile.domain.model.value;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.util.UUID;

public record ProfileId(UUID value) {
    public ProfileId {
        if (value == null) {
            throw new DomainValidationException("Profile id required");
        }
    }

    public static ProfileId getRandom() {
        return new ProfileId(UUID.randomUUID());
    }
}
