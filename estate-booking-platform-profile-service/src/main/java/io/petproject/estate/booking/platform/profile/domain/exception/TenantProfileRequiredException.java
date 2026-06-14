package io.petproject.estate.booking.platform.profile.domain.exception;

import java.util.UUID;

public final class TenantProfileRequiredException extends DomainStateException {
    private static final String CODE = "TENANT_PROFILE_REQUIRED";

    public TenantProfileRequiredException(UUID userId) {
        super(CODE, "User must have tenant profile to perform this operation: %s".formatted(userId));
    }
}
