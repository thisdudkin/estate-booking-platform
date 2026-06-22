package io.petproject.estate.booking.platform.identity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "application.integration.profile-service")
@Validated
public record ProfileServiceProperties(
    @NotNull
    URI baseUrl,
    @NotNull
    Duration connectTimeout,
    @NotNull
    Duration readTimeout
) { }
