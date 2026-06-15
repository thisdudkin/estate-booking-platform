package io.petproject.estate.booking.platform.profile.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddFavoriteListingRequest(

    @NotNull
    UUID listingId

) { }
