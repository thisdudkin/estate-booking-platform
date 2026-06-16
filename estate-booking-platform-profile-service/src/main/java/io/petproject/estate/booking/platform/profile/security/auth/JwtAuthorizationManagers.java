package io.petproject.estate.booking.platform.profile.security.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtAuthorizationManagers {
    private JwtAuthorizationManagers() {
        throw new AssertionError();
    }

    public static AuthorizationManager<RequestAuthorizationContext> hasAnyAuthority(
        Collection<String> requiredAuthorities
    ) {
        Set<String> required = Set.copyOf(requiredAuthorities);
        return (auth, context) -> {
            Authentication authentication = auth.get();
            if (isNotRealAuthentication(authentication)) {
                return new AuthorizationDecision(false);
            }
            Set<String> actual = authoritySet(authentication);
            boolean granted = required.stream()
                .anyMatch(actual::contains);
            return new AuthorizationDecision(granted);
        };
    }

    public static AuthorizationManager<RequestAuthorizationContext> hasAllAndAnyOf(
        Collection<String> allRequiredAuthorities,
        Collection<String> atLeastOneAuthority
    ) {
        Set<String> allRequired = Set.copyOf(allRequiredAuthorities);
        Set<String> anyOf = Set.copyOf(atLeastOneAuthority);
        return (auth, context) -> {
            Authentication authentication = auth.get();
            if (isNotRealAuthentication(authentication)) {
                return new AuthorizationDecision(false);
            }
            Set<String> actual = authoritySet(authentication);
            boolean hasAllRequired = actual.containsAll(allRequired);
            boolean hasTrustedCaller = anyOf.stream()
                .anyMatch(actual::contains);
            return new AuthorizationDecision(hasAllRequired && hasTrustedCaller);
        };
    }

    private static boolean isNotRealAuthentication(Authentication authentication) {
        return authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken;
    }

    private static Set<String> authoritySet(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toCollection(HashSet::new));
    }
}
