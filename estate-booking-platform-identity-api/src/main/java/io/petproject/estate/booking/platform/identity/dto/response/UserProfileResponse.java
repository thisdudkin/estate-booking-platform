package io.petproject.estate.booking.platform.identity.dto.response;

import io.petproject.estate.booking.platform.identity.dto.UserProfileStatus;

import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
    UUID profileId,
    UUID userId,
    String email,
    String phone,
    String displayName,
    UserProfileStatus status,
    Instant createdAt,
    Instant updatedAt
) { }
