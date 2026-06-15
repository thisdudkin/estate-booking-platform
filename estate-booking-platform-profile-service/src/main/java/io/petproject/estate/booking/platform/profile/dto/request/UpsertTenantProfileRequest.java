package io.petproject.estate.booking.platform.profile.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;

public record UpsertTenantProfileRequest(

    @Size(max = 128)
    String preferredCity,

    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    BigDecimal preferredMinPrice,

    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    BigDecimal preferredMaxPrice,

    JsonNode preferences

) { }
