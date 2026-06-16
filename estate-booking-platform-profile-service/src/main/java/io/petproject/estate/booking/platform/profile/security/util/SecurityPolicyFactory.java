package io.petproject.estate.booking.platform.profile.security.util;

import io.petproject.estate.booking.platform.profile.security.properties.SecurityProperties;
import io.petproject.estate.booking.platform.profile.security.properties.SecurityProperties.InternalApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class SecurityPolicyFactory {
    private SecurityPolicyFactory() {
        throw new AssertionError();
    }

    public static List<String> publicApiAuthorities(SecurityProperties properties) {
        return properties.publicApi()
            .allowedPlatformRoles()
            .stream()
            .map(SecurityAuthority::platformRole)
            .toList();
    }

    public static List<String> internalApiRequiredAuthorities(SecurityProperties properties) {
        InternalApi internalApi = properties.internalApi();
        List<String> required = new ArrayList<>();
        required.add(SecurityAuthority.servicePermission(
            properties.resourceId(),
            internalApi.requiredPermission()
        ));
        if (internalApi.audienceRequired()) {
            required.add(SecurityAuthority.audience(properties.resourceId()));
        }
        return List.copyOf(required);
    }

    public static List<String> trustedClientAuthorities(SecurityProperties properties) {
        return properties.internalApi()
            .trustedClientIds()
            .stream()
            .flatMap(clientId -> Stream.of(
                SecurityAuthority.clientId(clientId),
                SecurityAuthority.authorizedParty(clientId)
            ))
            .toList();
    }
}
