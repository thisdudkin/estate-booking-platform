package io.petproject.estate.booking.platform.profile.domain.model.favorite;

import io.petproject.estate.booking.platform.profile.domain.event.FavoriteListingAddedEvent;
import io.petproject.estate.booking.platform.profile.domain.event.FavoriteListingRemovedEvent;
import io.petproject.estate.booking.platform.profile.domain.model.AggregateRoot;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

import java.time.Instant;

public final class FavoriteListing extends AggregateRoot<FavoriteListingId> {
    private final FavoriteListingId id;
    private final Instant createdAt;

    private FavoriteListing(FavoriteListingId id, Instant createdAt) {
        this.id = DomainAssertions.requireNonNull(id, "id");
        this.createdAt = DomainAssertions.requireInstant(createdAt, "createdAt");
    }

    public static FavoriteListing create(UserId tenantId, ListingId listingId, Instant now) {
        DomainAssertions.requireInstant(now, "now");
        FavoriteListing favoriteListing = new FavoriteListing(
            new FavoriteListingId(tenantId, listingId),
            now
        );
        favoriteListing.registerEvent(FavoriteListingAddedEvent.of(
            tenantId,
            listingId,
            now
        ));
        return favoriteListing;
    }

    public static FavoriteListing restore(UserId tenantId, ListingId listingId, Instant createdAt) {
        return new FavoriteListing(
            new FavoriteListingId(tenantId, listingId),
            createdAt
        );
    }

    public void markRemoved(Instant now) {
        DomainAssertions.requireInstant(now, "now");
        registerEvent(FavoriteListingRemovedEvent.of(
            id.tenantId(),
            id.listingId(),
            now
        ));
    }

    @Override
    public FavoriteListingId id() {
        return id;
    }

    public UserId tenantId() {
        return id.tenantId();
    }

    public ListingId listingId() {
        return id.listingId();
    }

    public Instant createdAt() {
        return createdAt;
    }
}
