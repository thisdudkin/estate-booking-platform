package io.petproject.estate.booking.platform.keycloak.spi;

import java.io.Serializable;

record UserRegisteredPayload(
        String keycloakUserId,
        String email,
        String displayName,
        String role,
        boolean emailVerified
) implements Serializable { }
