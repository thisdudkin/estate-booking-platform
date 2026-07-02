package io.petproject.estate.booking.platform.profile.domain.model.value;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.util.UUID;

public record ListingId(UUID value) {
    public ListingId {
        if (value == null) {
            throw new DomainValidationException("Listing id required");
        }
    }
}
