package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;

import java.time.Instant;

public final class Tenant implements Auditable {

    private String preferredCity;
    private String contactName;
    private String contactPhone;
    private Instant updatedAt;

    private final Instant createdAt;

    private Tenant(
            String preferredCity,
            String contactName,
            String contactPhone,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.preferredCity = preferredCity;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static Tenant create(Instant timestamp) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        return new Tenant(
                null,
                null,
                null,
                timestamp,
                timestamp
        );
    }

    public void updatePreferences(
            String preferredCity,
            String contactName,
            String contactPhone,
            Instant timestamp
    ) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        this.preferredCity = preferredCity;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.updatedAt = timestamp;
    }

    public String getPreferredCity() {
        return preferredCity;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
