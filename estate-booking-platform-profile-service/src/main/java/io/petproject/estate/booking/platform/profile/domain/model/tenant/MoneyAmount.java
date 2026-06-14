package io.petproject.estate.booking.platform.profile.domain.model.tenant;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record MoneyAmount(BigDecimal value) {
    public MoneyAmount {
        DomainAssertions.requireNonNull(value, "moneyAmount");
        if (value.signum() < 0) {
            throw DomainValidationException.invalidField("moneyAmount", "must not be negative");
        }
        if (value.precision() - value.scale() > 10) {
            throw DomainValidationException.invalidField(
                "moneyAmount",
                "integer part is too large for numeric(12,2)"
            );
        }
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static MoneyAmount ofNullable(BigDecimal value) {
        return value == null ? null : new MoneyAmount(value);
    }
}
