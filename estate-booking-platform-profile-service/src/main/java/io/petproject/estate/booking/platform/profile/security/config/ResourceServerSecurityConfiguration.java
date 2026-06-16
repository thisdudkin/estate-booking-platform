package io.petproject.estate.booking.platform.profile.security.config;

import io.petproject.estate.booking.platform.profile.security.authority.JwtGrantedAuthoritiesExtractor;
import io.petproject.estate.booking.platform.profile.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static io.petproject.estate.booking.platform.profile.security.auth.JwtAuthorizationManagers.hasAllAndAnyOf;
import static io.petproject.estate.booking.platform.profile.security.auth.JwtAuthorizationManagers.hasAnyAuthority;
import static io.petproject.estate.booking.platform.profile.security.util.SecurityPolicyFactory.internalApiRequiredAuthorities;
import static io.petproject.estate.booking.platform.profile.security.util.SecurityPolicyFactory.publicApiAuthorities;
import static io.petproject.estate.booking.platform.profile.security.util.SecurityPolicyFactory.trustedClientAuthorities;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(JwtGrantedAuthoritiesExtractor.class)
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        SecurityProperties securityProperties,
        JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()

                .requestMatchers("/internal/api/**")
                .access(hasAllAndAnyOf(
                    internalApiRequiredAuthorities(securityProperties),
                    trustedClientAuthorities(securityProperties)
                ))

                .requestMatchers("/api/**")
                .access(hasAnyAuthority(
                    publicApiAuthorities(securityProperties)
                ))

                .anyRequest().denyAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
        JwtGrantedAuthoritiesExtractor authoritiesExtractor
    ) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesExtractor);
        return converter;
    }

}
