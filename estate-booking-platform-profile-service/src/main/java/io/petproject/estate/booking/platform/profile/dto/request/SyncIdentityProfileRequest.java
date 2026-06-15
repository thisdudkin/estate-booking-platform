package io.petproject.estate.booking.platform.profile.dto.request;

import io.petproject.estate.booking.platform.profile.entity.enums.UserProfileStatus;

public record SyncIdentityProfileRequest(

    UserProfileStatus status

) { }
