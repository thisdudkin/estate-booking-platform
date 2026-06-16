package io.petproject.estate.booking.platform.profile.security;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static io.petproject.estate.booking.platform.profile.security.SecurityUtil.ESTATE_PLATFORM_PREFIX;
import static io.petproject.estate.booking.platform.profile.security.SecurityUtil.PROFILE_SERVICE_PREFIX;
import static io.petproject.estate.booking.platform.profile.security.SecurityUtil.REALM_ROLES_CLAIM;
import static io.petproject.estate.booking.platform.profile.security.SecurityUtil.SERVICE_ROLES_CLAIM;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        List<String> roles = getStringListClaim(jwt, REALM_ROLES_CLAIM);
        List<String> serviceRoles = getStringListClaim(jwt, SERVICE_ROLES_CLAIM);
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        Stream.concat(roles.stream(), serviceRoles.stream())
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(role -> !role.isBlank())
            .filter(this::isApplicationAuthority)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
        return authorities;
    }

    private List<String> getStringListClaim(Jwt jwt, String claimName) {
        List<String> claim = jwt.getClaimAsStringList(claimName);
        return claim != null ? claim : Collections.emptyList();
    }

    private boolean isApplicationAuthority(String authority) {
        return authority.startsWith(ESTATE_PLATFORM_PREFIX) || authority.startsWith(PROFILE_SERVICE_PREFIX);
    }

}
