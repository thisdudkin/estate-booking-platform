package io.petproject.estate.booking.platform.profile.security.authority;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public interface JwtGrantedAuthoritiesExtractor
    extends Converter<Jwt, Collection<GrantedAuthority>> {
}
