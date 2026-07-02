package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.value.PhoneNumber;

import java.time.Instant;

public final class Landlord implements Auditable {

    private String publicName;
    private PhoneNumber contactPhone;
    private boolean verified;
    private Instant updatedAt;

    private final Instant createdAt;

    private Landlord(
            String publicName,
            PhoneNumber contactPhone,
            boolean verified,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.publicName = publicName;
        this.contactPhone = contactPhone;
        this.verified = verified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static Landlord create(Instant timestamp) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        return new Landlord(
                null,
                null,
                false,
                timestamp,
                timestamp
        );
    }

    public void verify(Instant timestamp) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        if (verified) {
            throw new DomainValidationException("Landlord is already validated");
        }
        this.verified = true;
        this.updatedAt = timestamp;
    }

    public void updateInfo(
            String publicName,
            PhoneNumber contactPhone,
            Instant timestamp
    ) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        this.publicName = publicName;
        this.contactPhone = contactPhone;
        this.updatedAt = timestamp;
    }

    public String getPublicName() {
        return publicName;
    }

    public PhoneNumber getContactPhone() {
        return contactPhone;
    }

    public boolean isVerified() {
        return verified;
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
