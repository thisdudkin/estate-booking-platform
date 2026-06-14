package io.petproject.estate.booking.platform.profile.domain.exception;

import java.util.UUID;

public final class UserProfileDeletedException extends DomainStateException {
    private static final String CODE = "USER_PROFILE_DELETED";

    public UserProfileDeletedException(UUID userId) {
        super(CODE, "Deleted user profile cannot be modified: %s".formatted(userId));
    }
}
