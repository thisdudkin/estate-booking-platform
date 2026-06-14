package io.petproject.estate.booking.platform.profile.domain.model.favorite;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.UUID;

public record ListingId(UUID value) {
    public ListingId {
        DomainAssertions.requireNonNull(value, "listingId");
    }
}
