package io.petproject.estate.booking.platform.profile.domain.model.value;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.exception.IllegalPhoneNumberException;

import java.util.regex.Pattern;

public record PhoneNumber(String number) {
    private static final Pattern PATTERN = Pattern.compile(
            "^\\+375(17|25|29|33|44)\\d{7}$"
    );

    public PhoneNumber {
        if (number == null) {
            throw new DomainValidationException("Phone number required");
        }
        if (!PATTERN.matcher(number).matches()) {
            throw new IllegalPhoneNumberException("Illegal phone number");
        }
    }
}
