package io.petproject.estate.booking.platform.profile.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security-provider.keycloak")
public record KeycloakSecurityProviderProperties(
    String realmRolePrefix,
    String realmAccessClaim,
    String resourceAccessClaim,
    String rolesClaim,
    String clientIdClaim,
    String authorizedPartyClaim
) { }
