package io.petproject.estate.booking.platform.profile.security;

final class SecurityUtil {

    private SecurityUtil() {
        throw new AssertionError();
    }

    static final String REALM_ROLES_CLAIM = "roles";
    static final String SERVICE_ROLES_CLAIM = "service_roles";

    static final String ESTATE_PLATFORM_PREFIX = "estate.platform.";
    static final String PROFILE_SERVICE_PREFIX = "profile-service.";

}
