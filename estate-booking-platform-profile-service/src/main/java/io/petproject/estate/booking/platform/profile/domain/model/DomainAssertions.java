package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.time.Instant;
import java.util.Collection;

public final class DomainAssertions {

    private DomainAssertions() {
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw DomainValidationException.invalidField(fieldName, "must not be null");
        }
        return value;
    }

    public static Instant requireInstant(Instant value, String fieldName) {
        return requireNonNull(value, fieldName);
    }

    public static String requireNonBlank(String value, String fieldName, int maxLength) {
        if (value == null || value.isBlank()) {
            throw DomainValidationException.invalidField(fieldName, "must not be blank");
        }
        String normalized = value.trim();
        if (normalized.length() > maxLength) {
            throw DomainValidationException.invalidField(
                fieldName,
                "length must be <= " + maxLength
            );
        }
        return normalized;
    }

    public static String normalizeNullable(String value, String fieldName, int maxLength) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.length() > maxLength) {
            throw DomainValidationException.invalidField(
                fieldName,
                "length must be <= " + maxLength
            );
        }
        return normalized;
    }

    public static void requireMaxSize(Collection<?> collection, String fieldName, int maxSize) {
        if (collection != null && collection.size() > maxSize) {
            throw DomainValidationException.invalidField(
                fieldName,
                "size must be <= " + maxSize
            );
        }
    }

}
