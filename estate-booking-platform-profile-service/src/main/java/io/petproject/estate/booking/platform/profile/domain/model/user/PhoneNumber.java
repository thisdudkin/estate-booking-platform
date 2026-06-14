package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.regex.Pattern;

public record PhoneNumber(String value) {
    private static final int MAX_LENGTH = 32;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9][0-9 .()\\-]{2,31}$");

    public PhoneNumber {
        value = DomainAssertions.requireNonBlank(value, "phone", MAX_LENGTH);
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw DomainValidationException.invalidField("phone", "has invalid format");
        }
    }

    public static PhoneNumber ofNullable(String value) {
        String normalized = DomainAssertions.normalizeNullable(value, "phone", MAX_LENGTH);
        return normalized == null ? null : new PhoneNumber(normalized);
    }
}
