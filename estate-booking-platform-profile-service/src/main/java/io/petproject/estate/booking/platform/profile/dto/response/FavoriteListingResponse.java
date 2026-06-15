package io.petproject.estate.booking.platform.profile.dto.response;

import java.time.Instant;
import java.util.UUID;

public record FavoriteListingResponse(
    UUID tenantId,
    UUID listingId,
    Instant createdAt
) { }
