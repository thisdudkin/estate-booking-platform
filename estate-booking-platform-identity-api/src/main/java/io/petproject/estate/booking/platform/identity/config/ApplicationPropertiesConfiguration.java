package io.petproject.estate.booking.platform.identity.config;

import io.petproject.estate.booking.platform.identity.config.properties.ProfileServiceProperties;
import io.petproject.estate.booking.platform.identity.config.properties.RegistrationProperties;
import io.petproject.estate.booking.platform.identity.config.properties.IdentityProviderProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
    RegistrationProperties.class,
    ProfileServiceProperties.class,
    IdentityProviderProperties.class
})
public class ApplicationPropertiesConfiguration { }
