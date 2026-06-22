package io.petproject.estate.booking.platform.identity.exception;

public class RegistrationFailedException extends RuntimeException {

    public RegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
