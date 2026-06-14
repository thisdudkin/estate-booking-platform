package io.petproject.estate.booking.platform.profile.domain.model.tenant;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

public record PreferredCity(String value) {
    private static final int MAX_LENGTH = 128;

    public PreferredCity {
        value = DomainAssertions.requireNonBlank(value, "preferredCity", MAX_LENGTH);
    }

    public static PreferredCity ofNullable(String value) {
        String normalized = DomainAssertions.normalizeNullable(value, "preferredCity", MAX_LENGTH);
        return normalized == null ? null : new PreferredCity(normalized);
    }
}
