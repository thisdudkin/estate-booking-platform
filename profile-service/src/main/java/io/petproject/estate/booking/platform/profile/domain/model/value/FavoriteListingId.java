package io.petproject.estate.booking.platform.profile.domain.model.value;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.util.UUID;

public record FavoriteListingId(UUID value) {
    public FavoriteListingId {
        if (value == null) {
            throw new DomainValidationException("Favorite listing id required");
        }
    }

    public static FavoriteListingId getRandom() {
        return new FavoriteListingId(UUID.randomUUID());
    }
}
