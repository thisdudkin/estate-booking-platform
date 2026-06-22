package io.petproject.estate.booking.platform.identity.config.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.identity-provider")
public record IdentityProviderProperties(
    @NotNull Type type
) {
    public enum Type {
        KEYCLOAK,
        GOOGLE
    }
}
