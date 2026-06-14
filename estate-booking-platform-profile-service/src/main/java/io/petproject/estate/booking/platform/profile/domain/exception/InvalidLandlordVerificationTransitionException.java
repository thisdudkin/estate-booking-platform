package io.petproject.estate.booking.platform.profile.domain.exception;

public final class InvalidLandlordVerificationTransitionException extends DomainStateException {
    private static final String CODE = "INVALID_LANDLORD_VERIFICATION_TRANSITION";

    public InvalidLandlordVerificationTransitionException(String message) {
        super(CODE, message);
    }
}
