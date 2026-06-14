package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.Locale;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final int MAX_LENGTH = 255;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public EmailAddress {
        value = DomainAssertions.requireNonBlank(value, "email", MAX_LENGTH)
            .toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw DomainValidationException.invalidField("email", "has invalid format");
        }
    }
}
