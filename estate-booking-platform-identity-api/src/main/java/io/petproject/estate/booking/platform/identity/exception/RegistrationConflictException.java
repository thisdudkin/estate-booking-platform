package io.petproject.estate.booking.platform.identity.exception;

public class RegistrationConflictException extends RuntimeException {

    public RegistrationConflictException(String message) {
        super(message);
    }
}
