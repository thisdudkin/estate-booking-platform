package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

public record DisplayName(String value) {
    private static final int MAX_LENGTH = 255;

    public DisplayName {
        value = DomainAssertions.requireNonBlank(value, "displayName", MAX_LENGTH);
    }

    public static DisplayName ofNullable(String value) {
        String normalized = DomainAssertions.normalizeNullable(value, "displayName", MAX_LENGTH);
        return normalized == null ? null : new DisplayName(normalized);
    }
}
