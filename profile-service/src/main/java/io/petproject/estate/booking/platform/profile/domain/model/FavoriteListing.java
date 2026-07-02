package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.value.FavoriteListingId;
import io.petproject.estate.booking.platform.profile.domain.model.value.ListingId;
import io.petproject.estate.booking.platform.profile.domain.model.value.ProfileId;

import java.time.Instant;

public final class FavoriteListing implements Auditable {

    private final FavoriteListingId id;
    private final ProfileId profileId;
    private final ListingId listingId;
    private final Instant createdAt;
    private final Instant updatedAt;

    private FavoriteListing(
            FavoriteListingId id,
            ProfileId profileId,
            ListingId listingId,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.profileId = profileId;
        this.listingId = listingId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FavoriteListing create(
            ProfileId profileId,
            ListingId listingId,
            Instant timestamp
    ) {
        if (profileId == null) {
            throw new DomainValidationException("Profile id required");
        }
        if (listingId == null) {
            throw new DomainValidationException("Listing id required");
        }
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        FavoriteListingId favoriteListingId = FavoriteListingId.getRandom();
        return new FavoriteListing(
                favoriteListingId,
                profileId,
                listingId,
                timestamp,
                timestamp
        );
    }

    public FavoriteListingId getId() {
        return id;
    }

    public ProfileId getProfileId() {
        return profileId;
    }

    public ListingId getListingId() {
        return listingId;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
