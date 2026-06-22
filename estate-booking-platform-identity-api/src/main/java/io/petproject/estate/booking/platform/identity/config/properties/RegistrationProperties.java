package io.petproject.estate.booking.platform.identity.config.properties;

import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationRole;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

@ConfigurationProperties(prefix = "application.registration")
@Validated
public record RegistrationProperties(
    @NotEmpty
    Map<RegistrationRole, String> roleGroups
) { }
