package io.petproject.estate.booking.platform.identity.service;

import io.petproject.estate.booking.platform.identity.client.ProfileServiceGateway;
import io.petproject.estate.booking.platform.identity.config.properties.RegistrationProperties;
import io.petproject.estate.booking.platform.identity.dto.UserProfileStatus;
import io.petproject.estate.booking.platform.identity.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.identity.dto.request.RegistrationRequest;
import io.petproject.estate.booking.platform.identity.dto.response.UserProfileResponse;
import io.petproject.estate.booking.platform.identity.entity.RegistrationAttempt;
import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationRole;
import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationStatus;
import io.petproject.estate.booking.platform.identity.exception.RegistrationConflictException;
import io.petproject.estate.booking.platform.identity.exception.RegistrationFailedException;
import io.petproject.estate.booking.platform.identity.provider.IdentityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final IdentityProvider identityProvider;
    private final ProfileServiceGateway profileServiceGateway;
    private final RegistrationAttemptService attemptService;
    private final RegistrationProperties registrationProperties;

    public void register(
        String idempotencyKey,
        RegistrationRequest rawRequest,
        RegistrationRole role
    ) {
        RegistrationRequest request = normalize(rawRequest);
        String requestHash = requestHash(request, role);
        RegistrationAttempt attempt = initializeAttempt(
            idempotencyKey, requestHash, request.email(), role);

        if (attempt.getStatus() == RegistrationStatus.COMPLETED) return;
        if (attempt.getStatus() == RegistrationStatus.PROFILE_CREATED) {
            attemptService.markCompleted(attempt.getId());
            return;
        }
        if (attempt.getStatus() != RegistrationStatus.STARTED
            && attempt.getStatus() != RegistrationStatus.USER_CREATED) {
            throw new RegistrationConflictException(
                "Registration cannot be resumed from status " + attempt.getStatus());
        }

        String group = registrationProperties.roleGroups().get(role);
        if (group == null || group.isBlank()) {
            throw new IllegalStateException("No identity provider group configured for role " + role);
        }

        if (attempt.getStatus() == RegistrationStatus.STARTED) {
            var userId = identityProvider.createUser(request, group, attempt.getId());
            attempt = attemptService.markUserCreated(attempt.getId(), userId);
        }

        CreateUserProfileRequest profileRequest = new CreateUserProfileRequest(
            attempt.getUserId(),
            request.email(),
            null,
            displayName(request),
            UserProfileStatus.ACTIVE
        );
        try {
            UserProfileResponse profile = profileServiceGateway.createProfile(profileRequest);
            attemptService.markProfileCreated(attempt.getId(), profile.profileId());
            attemptService.markCompleted(attempt.getId());
        } catch (RuntimeException profileException) {
            compensate(attempt, profileException);
        }
    }

    private RegistrationAttempt initializeAttempt(
        String idempotencyKey,
        String requestHash,
        String email,
        RegistrationRole role
    ) {
        try {
            return attemptService.create(idempotencyKey, requestHash, email, role);
        } catch (DataIntegrityViolationException exception) {
            return attemptService.resolveExisting(idempotencyKey, requestHash, email, role);
        }
    }

    private void compensate(RegistrationAttempt attempt, RuntimeException profileException) {
        attemptService.markCompensationPending(attempt.getId(), profileException);
        try {
            identityProvider.disableUser(attempt.getUserId());
            attemptService.markCompensated(attempt.getId());
        } catch (RuntimeException compensationException) {
            profileException.addSuppressed(compensationException);
            attemptService.markCompensationFailed(attempt.getId(), compensationException);
        }
        throw new RegistrationFailedException("Registration could not be completed", profileException);
    }

    private static RegistrationRequest normalize(RegistrationRequest request) {
        return new RegistrationRequest(
            request.email().strip().toLowerCase(Locale.ROOT),
            request.givenName().strip(),
            request.familyName().strip(),
            request.password()
        );
    }

    private static String displayName(RegistrationRequest request) {
        return request.givenName() + " " + request.familyName();
    }

    private static String requestHash(RegistrationRequest request, RegistrationRole role) {
        String canonicalRequest = String.join("\u001f",
            request.email(), request.givenName(), request.familyName(), role.name());
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(canonicalRequest.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
