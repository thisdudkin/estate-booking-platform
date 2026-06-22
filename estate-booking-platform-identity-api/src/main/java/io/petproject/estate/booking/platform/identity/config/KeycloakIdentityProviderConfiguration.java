package io.petproject.estate.booking.platform.identity.config;

import io.petproject.estate.booking.platform.identity.config.properties.KeycloakSecurityProviderProperties;
import io.petproject.estate.booking.platform.identity.provider.IdentityProvider;
import io.petproject.estate.booking.platform.identity.provider.keycloak.KeycloakTemplate;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KeycloakSecurityProviderProperties.class)
@ConditionalOnProperty(
    prefix = "application.identity-provider",
    name = "type",
    havingValue = "keycloak",
    matchIfMissing = true
)
public class KeycloakIdentityProviderConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    Keycloak keycloakAdminClient(KeycloakSecurityProviderProperties properties) {
        return KeycloakBuilder.builder()
            .serverUrl(properties.serverUrl().toString())
            .realm(properties.realm())
            .clientId(properties.clientId())
            .clientSecret(properties.clientSecret())
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(IdentityProvider.class)
    KeycloakTemplate keycloakTemplate(
        Keycloak keycloak,
        KeycloakSecurityProviderProperties properties
    ) {
        return new KeycloakTemplate(keycloak, properties.realm());
    }
}
