package io.petproject.estate.booking.platform.profile.domain.model.favorite;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

public record FavoriteListingId(
    UserId tenantId,
    ListingId listingId
) {
    public FavoriteListingId {
        DomainAssertions.requireNonNull(tenantId, "tenantId");
        DomainAssertions.requireNonNull(listingId, "listingId");
    }
}
