package io.petproject.estate.booking.platform.profile.dto.response;

import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TenantProfileResponse(
    UUID profileId,
    String preferredCity,
    BigDecimal preferredMinPrice,
    BigDecimal preferredMaxPrice,
    JsonNode preferences,
    Instant createdAt,
    Instant updatedAt
) { }
