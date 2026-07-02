package io.petproject.estate.booking.platform.profile.domain.model.value;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if (value == null) {
            throw new DomainValidationException("User id required");
        }
    }
}
