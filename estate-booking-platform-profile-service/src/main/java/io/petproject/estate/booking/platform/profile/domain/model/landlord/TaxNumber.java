package io.petproject.estate.booking.platform.profile.domain.model.landlord;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

public record TaxNumber(String value) {
    private static final int MAX_LENGTH = 64;

    public TaxNumber {
        value = DomainAssertions.requireNonBlank(value, "taxNumber", MAX_LENGTH);
    }

    public static TaxNumber ofNullable(String value) {
        String normalized = DomainAssertions.normalizeNullable(value, "taxNumber", MAX_LENGTH);
        return normalized == null ? null : new TaxNumber(normalized);
    }
}
