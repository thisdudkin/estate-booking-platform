package io.petproject.estate.booking.platform.profile.security.config;

import io.petproject.estate.booking.platform.profile.security.authority.JwtGrantedAuthoritiesExtractor;
import io.petproject.estate.booking.platform.profile.security.authority.KeycloakJwtGrantedAuthoritiesExtractor;
import io.petproject.estate.booking.platform.profile.security.properties.KeycloakSecurityProviderProperties;
import io.petproject.estate.booking.platform.profile.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
    SecurityProperties.class,
    KeycloakSecurityProviderProperties.class
})
@ConditionalOnProperty(
    prefix = "application.security",
    name = "provider",
    havingValue = "keycloak"
)
public class KeycloakSecurityProviderConfiguration {

    @Bean
    public JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor(
        SecurityProperties securityProperties,
        KeycloakSecurityProviderProperties keycloakProperties
    ) {
        return new KeycloakJwtGrantedAuthoritiesExtractor(
            securityProperties.resourceId(),
            keycloakProperties
        );
    }

}
