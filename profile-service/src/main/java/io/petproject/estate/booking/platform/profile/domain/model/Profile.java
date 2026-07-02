package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.exception.DomainValidationException;
import io.petproject.estate.booking.platform.profile.domain.model.value.PhoneNumber;
import io.petproject.estate.booking.platform.profile.domain.model.value.ProfileId;
import io.petproject.estate.booking.platform.profile.domain.model.value.UserId;

import java.time.Instant;

public final class Profile implements Auditable {

    private final ProfileId id;
    private final UserId userId;
    private final Instant createdAt;

    private String displayName;
    private PhoneNumber phoneNumber;
    private Instant updatedAt;

    private final Tenant tenant;
    private Landlord landlord;

    private Profile(
            ProfileId id,
            UserId userId,
            String displayName,
            PhoneNumber phoneNumber,
            Tenant tenant,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.tenant = tenant;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Profile create(
            UserId userId,
            String displayName,
            PhoneNumber phoneNumber,
            Instant timestamp
    ) {
        if (userId == null) {
            throw new DomainValidationException("User id required");
        }
        if (displayName == null) {
            throw new DomainValidationException("Display name required");
        }
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        ProfileId profileId = ProfileId.getRandom();
        Tenant tenant = Tenant.create(timestamp);
        return new Profile(
                profileId,
                userId,
                displayName,
                phoneNumber,
                tenant,
                timestamp,
                timestamp
        );
    }

    public void createLandlord(Instant timestamp) {
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        if (landlord != null) {
            throw new DomainValidationException("Landlord already exists");
        }
        this.landlord = Landlord.create(timestamp);
    }

    public void update(
            String displayName,
            PhoneNumber phoneNumber,
            Instant timestamp
    ) {
        if (displayName == null) {
            throw new DomainValidationException("Display name required");
        }
        if (timestamp == null) {
            throw new DomainValidationException("Timestamp required");
        }
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.updatedAt = timestamp;
    }

    public ProfileId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Landlord getLandlord() {
        return landlord;
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
