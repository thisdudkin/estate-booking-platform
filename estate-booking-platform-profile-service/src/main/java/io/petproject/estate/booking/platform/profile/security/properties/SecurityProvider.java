package io.petproject.estate.booking.platform.profile.security.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityProvider {
    KEYCLOAK("KEYCLOAK");

    private final String propertyValue;
}
