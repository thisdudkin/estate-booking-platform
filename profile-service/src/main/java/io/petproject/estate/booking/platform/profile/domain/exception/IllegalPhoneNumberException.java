package io.petproject.estate.booking.platform.profile.domain.exception;

public final class IllegalPhoneNumberException extends DomainValidationException {
    public IllegalPhoneNumberException(String message) {
        super(message);
    }
}
