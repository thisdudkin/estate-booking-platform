package io.petproject.estate.booking.platform.profile.dto.response;

public record ProfileDetailsResponse(
    UserProfileResponse profile,
    TenantProfileResponse tenant,
    LandlordProfileResponse landlord
) { }
