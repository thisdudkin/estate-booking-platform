package io.petproject.estate.booking.platform.profile.domain.exception;

import java.util.UUID;

public final class UserProfileNotActiveException extends DomainStateException {
    private static final String CODE = "USER_PROFILE_NOT_ACTIVE";

    public UserProfileNotActiveException(UUID userId) {
        super(CODE, "User profile is not active: %s".formatted(userId));
    }
}
