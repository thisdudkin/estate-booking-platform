package io.petproject.estate.booking.platform.profile.exception;

public class UserProfileAlreadyExistsException extends RuntimeException {
    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }

    public UserProfileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
