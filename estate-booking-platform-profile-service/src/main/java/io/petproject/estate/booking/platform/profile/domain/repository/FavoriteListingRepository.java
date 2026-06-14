package io.petproject.estate.booking.platform.profile.domain.repository;

import io.petproject.estate.booking.platform.profile.domain.model.favorite.FavoriteListing;
import io.petproject.estate.booking.platform.profile.domain.model.favorite.FavoriteListingId;
import io.petproject.estate.booking.platform.profile.domain.model.favorite.ListingId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

import java.util.Optional;

public interface FavoriteListingRepository {
    Optional<FavoriteListing> findById(FavoriteListingId id);

    default Optional<FavoriteListing> findByTenantIdAndListingId(
        UserId tenantId,
        ListingId listingId
    ) {
        return findById(new FavoriteListingId(tenantId, listingId));
    }

    boolean existsById(FavoriteListingId id);

    default boolean existsByTenantIdAndListingId(
        UserId tenantId,
        ListingId listingId
    ) {
        return existsById(new FavoriteListingId(tenantId, listingId));
    }

    void save(FavoriteListing favoriteListing);

    void remove(FavoriteListing favoriteListing);
}
