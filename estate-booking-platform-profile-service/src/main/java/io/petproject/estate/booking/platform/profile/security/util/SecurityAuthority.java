package io.petproject.estate.booking.platform.profile.security.util;

public final class SecurityAuthority {
    private SecurityAuthority() {
        throw new AssertionError();
    }

    public static final String AUDIENCE_PREFIX = "AUD:";
    public static final String CLIENT_ID_PREFIX = "CLIENT_ID:";
    public static final String AUTHORIZED_PARTY_PREFIX = "AZP:";
    public static final String PLATFORM_ROLE_PREFIX = "PLATFORM_ROLE:";
    public static final String SERVICE_PERMISSION_PREFIX = "SERVICE_PERMISSION:";

    public static String platformRole(String role) {
        return PLATFORM_ROLE_PREFIX + role;
    }

    public static String servicePermission(String resourceId, String permission) {
        return SERVICE_PERMISSION_PREFIX + resourceId + ":" + permission;
    }

    public static String audience(String audience) {
        return AUDIENCE_PREFIX + audience;
    }

    public static String clientId(String clientId) {
        return CLIENT_ID_PREFIX + clientId;
    }

    public static String authorizedParty(String authorizedParty) {
        return AUTHORIZED_PARTY_PREFIX + authorizedParty;
    }
}
