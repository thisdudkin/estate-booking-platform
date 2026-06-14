package io.petproject.estate.booking.platform.profile.domain.exception;

public final class DomainValidationException extends DomainException {
    private static final String CODE = "DOMAIN_VALIDATION_EXCEPTION";

    public DomainValidationException(String message) {
        super(CODE, message);
    }

    public static DomainValidationException invalidField(String fieldName, String reason) {
        return new DomainValidationException("Invalid field '%s': %s".formatted(fieldName, reason));
    }
}
