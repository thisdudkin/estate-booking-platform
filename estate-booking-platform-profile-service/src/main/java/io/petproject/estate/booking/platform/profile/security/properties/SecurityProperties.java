package io.petproject.estate.booking.platform.profile.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application.security")
public record SecurityProperties(
    String provider,
    String resourceId,
    PublicApi publicApi,
    InternalApi internalApi
) {
    public record PublicApi(List<String> allowedPlatformRoles) {
    }

    public record InternalApi(
        boolean audienceRequired,
        String requiredPermission,
        List<String> trustedClientIds
    ) {
    }
}
