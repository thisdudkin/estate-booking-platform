package io.petproject.estate.booking.platform.profile.domain.model.landlord;

import io.petproject.estate.booking.platform.profile.domain.exception.InvalidLandlordVerificationTransitionException;
import io.petproject.estate.booking.platform.profile.domain.model.DomainAssertions;

import java.time.Instant;
import java.util.Optional;

public final class LandlordProfile {
    private final Instant createdAt;

    private CompanyName companyName;
    private VerificationStatus verificationStatus;
    private TaxNumber taxNumber;
    private Instant updatedAt;

    private LandlordProfile(
        CompanyName companyName,
        VerificationStatus verificationStatus,
        TaxNumber taxNumber,
        Instant createdAt,
        Instant updatedAt
    ) {
        this.companyName = companyName;
        this.verificationStatus = DomainAssertions.requireNonNull(
            verificationStatus,
            "verificationStatus"
        );
        this.taxNumber = taxNumber;
        this.createdAt = DomainAssertions.requireInstant(createdAt, "createdAt");
        this.updatedAt = DomainAssertions.requireInstant(updatedAt, "updatedAt");
    }

    public static LandlordProfile create(CompanyName companyName, TaxNumber taxNumber, Instant now) {
        DomainAssertions.requireInstant(now, "now");
        return new LandlordProfile(
            companyName,
            VerificationStatus.UNVERIFIED,
            taxNumber,
            now,
            now
        );
    }

    public static LandlordProfile restore(CompanyName companyName, VerificationStatus verificationStatus, TaxNumber taxNumber, Instant createdAt, Instant updatedAt) {
        return new LandlordProfile(
            companyName,
            verificationStatus,
            taxNumber,
            createdAt,
            updatedAt
        );
    }

    public void updateCompanyDetails(CompanyName companyName, TaxNumber taxNumber, Instant now) {
        DomainAssertions.requireInstant(now, "now");
        this.companyName = companyName;
        this.taxNumber = taxNumber;
        this.updatedAt = now;
    }

    public void submitForVerification(Instant now) {
        DomainAssertions.requireInstant(now, "now");
        if (verificationStatus == VerificationStatus.VERIFIED) {
            throw new InvalidLandlordVerificationTransitionException(
                "Verified landlord profile cannot be submitted for verification again"
            );
        }
        if (verificationStatus == VerificationStatus.PENDING) {
            return;
        }
        this.verificationStatus = VerificationStatus.PENDING;
        this.updatedAt = now;
    }

    public void verify(Instant now) {
        DomainAssertions.requireInstant(now, "now");
        if (verificationStatus != VerificationStatus.PENDING) {
            throw new InvalidLandlordVerificationTransitionException(
                "Only pending landlord profile can be verified"
            );
        }
        this.verificationStatus = VerificationStatus.VERIFIED;
        this.updatedAt = now;
    }

    public void reject(Instant now) {
        DomainAssertions.requireInstant(now, "now");
        if (verificationStatus != VerificationStatus.PENDING) {
            throw new InvalidLandlordVerificationTransitionException(
                "Only pending landlord profile can be rejected"
            );
        }
        this.verificationStatus = VerificationStatus.REJECTED;
        this.updatedAt = now;
    }

    public Optional<CompanyName> companyName() {
        return Optional.ofNullable(companyName);
    }

    public VerificationStatus verificationStatus() {
        return verificationStatus;
    }

    public Optional<TaxNumber> taxNumber() {
        return Optional.ofNullable(taxNumber);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
