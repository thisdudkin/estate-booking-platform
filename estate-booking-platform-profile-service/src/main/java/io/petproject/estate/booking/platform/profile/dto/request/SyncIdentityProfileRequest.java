package io.petproject.estate.booking.platform.profile.dto.request;

import io.petproject.estate.booking.platform.profile.entity.enums.UserProfileStatus;
import jakarta.validation.constraints.Email;

public record SyncIdentityProfileRequest(

    @Email
    String email,

    UserProfileStatus status

) { }
