package io.petproject.estate.booking.platform.profile.domain.model.tenant;

import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.time.Instant;
import java.util.Optional;

public final class TenantProfile {
    private final Instant createdAt;

    private PreferredCity preferredCity;
    private PriceRange preferredPriceRange;
    private TenantPreferences preferences;
    private Instant updatedAt;

    private TenantProfile(
        PreferredCity preferredCity,
        PriceRange preferredPriceRange,
        TenantPreferences preferences,
        Instant createdAt,
        Instant updatedAt
    ) {
        this.preferredCity = preferredCity;
        this.preferredPriceRange = preferredPriceRange == null
            ? PriceRange.unrestricted()
            : preferredPriceRange;
        this.preferences = preferences == null
            ? TenantPreferences.empty()
            : preferences;
        this.createdAt = DomainAssertions.requireInstant(createdAt, "createdAt");
        this.updatedAt = DomainAssertions.requireInstant(updatedAt, "updatedAt");
    }

    public static TenantProfile create(
        PreferredCity preferredCity,
        PriceRange preferredPriceRange,
        TenantPreferences preferences,
        Instant now
    ) {
        DomainAssertions.requireInstant(now, "now");
        return new TenantProfile(
            preferredCity,
            preferredPriceRange,
            preferences,
            now,
            now
        );
    }

    public static TenantProfile restore(
        PreferredCity preferredCity,
        PriceRange preferredPriceRange,
        TenantPreferences preferences,
        Instant createdAt,
        Instant updatedAt
    ) {
        return new TenantProfile(
            preferredCity,
            preferredPriceRange,
            preferences,
            createdAt,
            updatedAt
        );
    }

    public void updatePreferences(
        PreferredCity preferredCity,
        PriceRange preferredPriceRange,
        TenantPreferences preferences,
        Instant now
    ) {
        DomainAssertions.requireInstant(now, "now");
        this.preferredCity = preferredCity;
        this.preferredPriceRange = preferredPriceRange == null
            ? PriceRange.unrestricted()
            : preferredPriceRange;
        this.preferences = preferences == null
            ? TenantPreferences.empty()
            : preferences;
        this.updatedAt = now;
    }

    public Optional<PreferredCity> preferredCity() {
        return Optional.ofNullable(preferredCity);
    }

    public PriceRange preferredPriceRange() {
        return preferredPriceRange;
    }

    public TenantPreferences preferences() {
        return preferences;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
