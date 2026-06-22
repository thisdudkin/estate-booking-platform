package io.petproject.estate.booking.platform.identity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;

@ConfigurationProperties(prefix = "application.security-provider.keycloak")
@Validated
public record KeycloakSecurityProviderProperties(
    @NotNull
    URI serverUrl,
    @NotBlank
    String realm,
    @NotBlank
    String clientId,
    @NotBlank
    String clientSecret
) { }
