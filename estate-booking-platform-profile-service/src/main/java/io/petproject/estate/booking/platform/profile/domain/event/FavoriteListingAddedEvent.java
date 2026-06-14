package io.petproject.estate.booking.platform.profile.domain.event;

import io.petproject.estate.booking.platform.profile.domain.model.favorite.ListingId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;

import java.time.Instant;
import java.util.UUID;

public record FavoriteListingAddedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID tenantId,
    UUID listingId,
    Instant occurredAt
) implements DomainEvent {
    private static final String AGGREGATE_TYPE = "FAVORITE_LISTING";
    private static final String EVENT_TYPE = "FAVORITE_LISTING_ADDED";

    public static FavoriteListingAddedEvent of(
        UserId tenantId,
        ListingId listingId,
        Instant occurredAt
    ) {
        return new FavoriteListingAddedEvent(
            UUID.randomUUID(),
            tenantId.value(),
            tenantId.value(),
            listingId.value(),
            occurredAt
        );
    }

    @Override
    public String aggregateType() {
        return AGGREGATE_TYPE;
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
