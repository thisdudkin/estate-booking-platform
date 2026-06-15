package io.petproject.estate.booking.platform.profile.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(

    @Size(max = 32)
    String phone,

    @Size(max = 255)
    String displayName

) { }
