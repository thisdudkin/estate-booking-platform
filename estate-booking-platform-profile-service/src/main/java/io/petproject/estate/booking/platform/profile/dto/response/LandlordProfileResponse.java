package io.petproject.estate.booking.platform.profile.dto.response;

import io.petproject.estate.booking.platform.profile.entity.enums.VerificationStatus;

import java.time.Instant;
import java.util.UUID;

public record LandlordProfileResponse(
    UUID profileId,
    String companyName,
    VerificationStatus verificationStatus,
    String taxNumber,
    Instant createdAt,
    Instant updatedAt
) { }
