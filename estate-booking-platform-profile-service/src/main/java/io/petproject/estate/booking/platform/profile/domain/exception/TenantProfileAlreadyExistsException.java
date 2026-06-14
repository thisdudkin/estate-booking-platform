package io.petproject.estate.booking.platform.profile.domain.exception;

import java.util.UUID;

public final class TenantProfileAlreadyExistsException extends DomainStateException {
    private static final String CODE = "TENANT_PROFILE_ALREADY_EXISTS";

    public TenantProfileAlreadyExistsException(UUID userId) {
        super(CODE, "Tenant profile already exists for user: %s".formatted(userId));
    }
}
