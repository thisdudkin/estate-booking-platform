package io.petproject.estate.booking.platform.profile.domain.exception;

import java.util.UUID;

public final class LandlordProfileAlreadyExistsException extends DomainStateException {
    private static final String CODE = "LANDLORD_PROFILE_ALREADY_EXISTS";

    public LandlordProfileAlreadyExistsException(UUID userId) {
        super(CODE, "Landlord profile already exists for user: %s".formatted(userId));
    }
}
