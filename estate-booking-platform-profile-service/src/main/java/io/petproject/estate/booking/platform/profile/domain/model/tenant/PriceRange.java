package io.petproject.estate.booking.platform.profile.domain.model.tenant;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.util.Optional;

public record PriceRange(MoneyAmount min, MoneyAmount max) {
    public PriceRange {
        if (min != null && max != null && min.value().compareTo(max.value()) > 0) {
            throw DomainValidationException.invalidField(
                "priceRange",
                "min price must be less than or equal to max price"
            );
        }
    }

    public static PriceRange unrestricted() {
        return new PriceRange(null, null);
    }

    public Optional<MoneyAmount> minPrice() {
        return Optional.ofNullable(min);
    }

    public Optional<MoneyAmount> maxPrice() {
        return Optional.ofNullable(max);
    }
}
