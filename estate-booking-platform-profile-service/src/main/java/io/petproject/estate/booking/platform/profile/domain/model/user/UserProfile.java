package io.petproject.estate.booking.platform.profile.domain.model.user;

import io.petproject.estate.booking.platform.profile.domain.event.LandlordProfileAttachedEvent;
import io.petproject.estate.booking.platform.profile.domain.event.TenantProfileAttachedEvent;
import io.petproject.estate.booking.platform.profile.domain.event.UserProfileCreatedEvent;
import io.petproject.estate.booking.platform.profile.domain.event.UserProfileStatusChangedEvent;
import io.petproject.estate.booking.platform.profile.domain.exception.LandlordProfileAlreadyExistsException;
import io.petproject.estate.booking.platform.profile.domain.exception.TenantProfileAlreadyExistsException;
import io.petproject.estate.booking.platform.profile.domain.exception.TenantProfileRequiredException;
import io.petproject.estate.booking.platform.profile.domain.exception.UserProfileDeletedException;
import io.petproject.estate.booking.platform.profile.domain.exception.UserProfileNotActiveException;
import io.petproject.estate.booking.platform.profile.domain.model.AggregateRoot;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;
import io.petproject.estate.booking.platform.profile.domain.model.landlord.LandlordProfile;
import io.petproject.estate.booking.platform.profile.domain.model.tenant.TenantProfile;

import java.time.Instant;
import java.util.Optional;

public final class UserProfile extends AggregateRoot<UserId> {
    private final UserId id;
    private final KeycloakUserId keycloakUserId;
    private final Instant createdAt;

    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;
    private DisplayName displayName;
    private UserStatus status;
    private TenantProfile tenantProfile;
    private LandlordProfile landlordProfile;
    private Instant updatedAt;

    private UserProfile(
        UserId id,
        KeycloakUserId keycloakUserId,
        EmailAddress email,
        PhoneNumber phone,
        DisplayName displayName,
        UserStatus status,
        TenantProfile tenantProfile,
        LandlordProfile landlordProfile,
        Instant createdAt,
        Instant updatedAt
    ) {
        this.id = DomainAssertions.requireNonNull(id, "id");
        this.keycloakUserId = DomainAssertions.requireNonNull(keycloakUserId, "keycloakUserId");
        this.emailAddress = DomainAssertions.requireNonNull(email, "email");
        this.phoneNumber = phone;
        this.displayName = displayName;
        this.status = DomainAssertions.requireNonNull(status, "status");
        this.tenantProfile = tenantProfile;
        this.landlordProfile = landlordProfile;
        this.createdAt = DomainAssertions.requireInstant(createdAt, "createdAt");
        this.updatedAt = DomainAssertions.requireInstant(updatedAt, "updatedAt");
    }

    public static UserProfile create(
        UserId id,
        KeycloakUserId keycloakUserId,
        EmailAddress email,
        PhoneNumber phone,
        DisplayName displayName,
        Instant now
    ) {
        DomainAssertions.requireInstant(now, "now");
        UserProfile profile = new UserProfile(
            id,
            keycloakUserId,
            email,
            phone,
            displayName,
            UserStatus.ACTIVE,
            null,
            null,
            now,
            now
        );
        profile.registerEvent(UserProfileCreatedEvent.of(
            profile.id,
            profile.keycloakUserId,
            profile.emailAddress,
            now
        ));
        return profile;
    }

    public static UserProfile restore(
        UserId id,
        KeycloakUserId keycloakUserId,
        EmailAddress email,
        PhoneNumber phone,
        DisplayName displayName,
        UserStatus status,
        TenantProfile tenantProfile,
        LandlordProfile landlordProfile,
        Instant createdAt,
        Instant updatedAt
    ) {
        return new UserProfile(
            id,
            keycloakUserId,
            email,
            phone,
            displayName,
            status,
            tenantProfile,
            landlordProfile,
            createdAt,
            updatedAt
        );
    }

    public void changeEmail(EmailAddress newEmail, Instant now) {
        ensureNotDeleted();
        DomainAssertions.requireInstant(now, "now");
        DomainAssertions.requireNonNull(newEmail, "newEmail");
        if (emailAddress.equals(newEmail)) {
            return;
        }
        this.emailAddress = newEmail;
        touch(now);
    }

    public void changePhone(PhoneNumber newPhone, Instant now) {
        ensureNotDeleted();
        DomainAssertions.requireInstant(now, "now");
        if ((phoneNumber == null && newPhone == null) || (phoneNumber != null && phoneNumber.equals(newPhone))) {
            return;
        }
        this.phoneNumber = newPhone;
        touch(now);
    }

    public void changeDisplayName(DisplayName newDisplayName, Instant now) {
        ensureNotDeleted();
        DomainAssertions.requireInstant(now, "now");
        if ((displayName == null && newDisplayName == null)
            || (displayName != null && displayName.equals(newDisplayName))) {
            return;
        }
        this.displayName = newDisplayName;
        touch(now);
    }

    public void activate(Instant now) {
        ensureNotDeleted();
        changeStatus(UserStatus.ACTIVE, now);
    }

    public void suspend(Instant now) {
        ensureNotDeleted();
        changeStatus(UserStatus.SUSPENDED, now);
    }

    public void delete(Instant now) {
        changeStatus(UserStatus.DELETED, now);
    }

    public void attachTenantProfile(TenantProfile tenantProfile, Instant now) {
        ensureActive();
        DomainAssertions.requireInstant(now, "now");
        DomainAssertions.requireNonNull(tenantProfile, "tenantProfile");
        if (this.tenantProfile != null) {
            throw new TenantProfileAlreadyExistsException(id.value());
        }
        this.tenantProfile = tenantProfile;
        touch(now);
        registerEvent(TenantProfileAttachedEvent.of(id, now));
    }

    public void attachLandlordProfile(LandlordProfile landlordProfile, Instant now) {
        ensureActive();
        DomainAssertions.requireInstant(now, "now");
        DomainAssertions.requireNonNull(landlordProfile, "landlordProfile");
        if (this.landlordProfile != null) {
            throw new LandlordProfileAlreadyExistsException(id.value());
        }
        this.landlordProfile = landlordProfile;
        touch(now);
        registerEvent(LandlordProfileAttachedEvent.of(id, now));
    }

    public void ensureTenantProfileExists() {
        if (tenantProfile == null) {
            throw new TenantProfileRequiredException(id.value());
        }
    }

    public boolean isTenant() {
        return tenantProfile != null;
    }

    public boolean isLandlord() {
        return landlordProfile != null;
    }

    private void changeStatus(UserStatus newStatus, Instant now) {
        DomainAssertions.requireInstant(now, "now");
        DomainAssertions.requireNonNull(newStatus, "newStatus");
        if (status == newStatus) {
            return;
        }
        UserStatus oldStatus = this.status;
        this.status = newStatus;
        touch(now);
        registerEvent(UserProfileStatusChangedEvent.of(id, oldStatus, newStatus, now));
    }

    private void ensureActive() {
        ensureNotDeleted();
        if (!status.isActive()) {
            throw new UserProfileNotActiveException(id.value());
        }
    }

    private void ensureNotDeleted() {
        if (status.isDeleted()) {
            throw new UserProfileDeletedException(id.value());
        }
    }

    private void touch(Instant now) {
        this.updatedAt = now;
    }

    @Override
    public UserId id() {
        return id;
    }

    public KeycloakUserId keycloakUserId() {
        return keycloakUserId;
    }

    public EmailAddress emailAddress() {
        return emailAddress;
    }

    public Optional<PhoneNumber> phoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public Optional<DisplayName> displayName() {
        return Optional.ofNullable(displayName);
    }

    public UserStatus status() {
        return status;
    }

    public Optional<TenantProfile> tenantProfile() {
        return Optional.ofNullable(tenantProfile);
    }

    public Optional<LandlordProfile> landlordProfile() {
        return Optional.ofNullable(landlordProfile);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
