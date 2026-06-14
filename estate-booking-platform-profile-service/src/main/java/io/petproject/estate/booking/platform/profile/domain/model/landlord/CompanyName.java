package io.petproject.estate.booking.platform.profile.domain.model.landlord;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

public record CompanyName(String value) {
    private static final int MAX_LENGTH = 255;

    public CompanyName {
        value = DomainAssertions.requireNonBlank(value, "companyName", MAX_LENGTH);
    }

    public static CompanyName ofNullable(String value) {
        String normalized = DomainAssertions.normalizeNullable(value, "companyName", MAX_LENGTH);
        return normalized == null ? null : new CompanyName(normalized);
    }
}
