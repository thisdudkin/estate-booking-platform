package io.petproject.estate.booking.platform.profile.dto.request;

import io.petproject.estate.booking.platform.profile.entity.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLandlordVerificationStatusRequest(

    @NotNull
    VerificationStatus verificationStatus

) { }
