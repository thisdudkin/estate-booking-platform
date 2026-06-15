package io.petproject.estate.booking.platform.profile.dto.request;

import jakarta.validation.constraints.Size;

public record UpsertLandlordProfileRequest(

    @Size(max = 255)
    String companyName,

    @Size(max = 64)
    String taxNumber

) { }
