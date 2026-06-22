package io.petproject.estate.booking.platform.identity.exception;

public class IdentityProviderConflictException extends IdentityProviderException {

    public IdentityProviderConflictException(String message) {
        super(message, null);
    }
}
