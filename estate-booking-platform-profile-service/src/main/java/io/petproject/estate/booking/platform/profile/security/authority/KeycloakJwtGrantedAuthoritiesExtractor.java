package io.petproject.estate.booking.platform.profile.security.authority;

import io.petproject.estate.booking.platform.profile.security.properties.KeycloakSecurityProviderProperties;
import io.petproject.estate.booking.platform.profile.security.util.SecurityAuthority;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class KeycloakJwtGrantedAuthoritiesExtractor
    implements JwtGrantedAuthoritiesExtractor {

    private final String resourceId;
    private final KeycloakSecurityProviderProperties properties;

    public KeycloakJwtGrantedAuthoritiesExtractor(
        String resourceId,
        KeycloakSecurityProviderProperties properties
    ) {
        this.resourceId = resourceId;
        this.properties = properties;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        addPlatformRoleAuthorities(jwt, authorities);
        addServicePermissionAuthorities(jwt, authorities);
        addAudienceAuthorities(jwt, authorities);
        addClientIdentityAuthorities(jwt, authorities);
        return authorities;
    }

    private void addPlatformRoleAuthorities(Jwt jwt, Set<GrantedAuthority> authorities) {
        extractRealmRoles(jwt).stream()
            .map(realmRole -> {
                if (!StringUtils.hasText(realmRole)) {
                    return null;
                }
                if (!realmRole.startsWith(properties.realmRolePrefix())) {
                    return null;
                }
                String platformRole = realmRole.substring(properties.realmRolePrefix().length());
                return StringUtils.hasText(platformRole) ? platformRole : null;
            })
            .filter(StringUtils::hasText)
            .map(SecurityAuthority::platformRole)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
    }

    private Set<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(properties.realmAccessClaim());
        if (realmAccess == null) {
            return Collections.emptySet();
        }
        return extractStringSet(realmAccess.get(properties.rolesClaim()));
    }

    private Set<String> extractStringSet(Object value) {
        if (!(value instanceof Collection<?> values)) {
            return Collections.emptySet();
        }
        Set<String> result = new LinkedHashSet<>();
        values.forEach(item -> {
            if (item instanceof String text) {
                String normalized = text.trim();
                if (StringUtils.hasText(normalized)) {
                    result.add(normalized);
                }
            }
        });
        return result;
    }

    private void addServicePermissionAuthorities(Jwt jwt, Set<GrantedAuthority> authorities) {
        extractClientRoles(jwt, resourceId).stream()
            .map(permission -> SecurityAuthority.servicePermission(resourceId, permission))
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
    }

    private Set<String> extractClientRoles(Jwt jwt, String clientId) {
        Map<String, Object> resourceAccess = jwt.getClaim(properties.resourceAccessClaim());
        if (resourceAccess == null) {
            return Collections.emptySet();
        }
        Object clientAccessObject = resourceAccess.get(clientId);
        if (!(clientAccessObject instanceof Map<?, ?> clientAccess)) {
            return Collections.emptySet();
        }
        return extractStringSet(clientAccess.get(properties.rolesClaim()));
    }

    private void addAudienceAuthorities(Jwt jwt, Set<GrantedAuthority> authorities) {
        List<String> audience = jwt.getAudience();
        if (audience == null) {
            return;
        }
        audience.stream()
            .filter(StringUtils::hasText)
            .map(SecurityAuthority::audience)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
    }

    private void addClientIdentityAuthorities(Jwt jwt, Set<GrantedAuthority> authorities) {
        String clientId = jwt.getClaimAsString(properties.clientIdClaim());
        if (StringUtils.hasText(clientId)) {
            authorities.add(new SimpleGrantedAuthority(SecurityAuthority.clientId(clientId)));
        }
        String authorizedParty = jwt.getClaimAsString(properties.authorizedPartyClaim());
        if (StringUtils.hasText(authorizedParty)) {
            authorities.add(new SimpleGrantedAuthority(SecurityAuthority.authorizedParty(authorizedParty)));
        }
    }
}
