package io.petproject.estate.booking.platform.profile.domain.exception;

public class DomainStateException extends DomainException {
    public DomainStateException(String code, String message) {
        super(code, message);
    }
}
