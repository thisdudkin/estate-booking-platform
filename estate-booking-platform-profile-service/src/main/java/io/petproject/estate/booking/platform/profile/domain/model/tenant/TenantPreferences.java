package io.petproject.estate.booking.platform.profile.domain.model.tenant;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.util.Set;
import java.util.stream.Collectors;

public record TenantPreferences(
    Set<String> propertyTypes,
    Set<String> amenities,
    Boolean petsAllowed,
    Boolean furnished
) {
    private static final int MAX_PROPERTY_TYPES = 20;
    private static final int MAX_AMENITIES = 50;
    private static final int MAX_ITEM_LENGTH = 64;

    public TenantPreferences {
        propertyTypes = normalizeSet(propertyTypes, "propertyTypes", MAX_PROPERTY_TYPES);
        amenities = normalizeSet(amenities, "amenities", MAX_AMENITIES);
    }

    public static TenantPreferences empty() {
        return new TenantPreferences(Set.of(), Set.of(), null, null);
    }

    private static Set<String> normalizeSet(Set<String> source, String fieldName, int maxSize) {
        if (source == null || source.isEmpty()) {
            return Set.of();
        }
        DomainAssertions.requireMaxSize(source, fieldName, maxSize);
        return source.stream()
            .map(value -> {
                if (value == null || value.isBlank()) {
                    throw DomainValidationException.invalidField(fieldName, "must not contain blank values");
                }
                String normalized = value.trim();
                if (normalized.length() > MAX_ITEM_LENGTH) {
                    throw DomainValidationException.invalidField(
                        fieldName,
                        "item length must be <= " + MAX_ITEM_LENGTH
                    );
                }
                return normalized;
            })
            .collect(Collectors.toUnmodifiableSet());
    }
}
