package io.petproject.estate.booking.platform.identity.service;

import io.petproject.estate.booking.platform.identity.entity.RegistrationAttempt;
import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationRole;
import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationStatus;
import io.petproject.estate.booking.platform.identity.exception.RegistrationConflictException;
import io.petproject.estate.booking.platform.identity.repository.RegistrationAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationAttemptService {

    private final RegistrationAttemptRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RegistrationAttempt create(
        String idempotencyKey,
        String requestHash,
        String email,
        RegistrationRole role
    ) {
        RegistrationAttempt attempt = RegistrationAttempt.builder()
            .idempotencyKey(idempotencyKey)
            .requestHash(requestHash)
            .email(email)
            .requestedRole(role)
            .status(RegistrationStatus.STARTED)
            .build();
        return repository.saveAndFlush(attempt);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public RegistrationAttempt resolveExisting(
        String idempotencyKey,
        String requestHash,
        String email,
        RegistrationRole role
    ) {
        RegistrationAttempt attempt = repository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> repository.findByEmail(email)
                .orElseThrow(() -> new RegistrationConflictException(
                    "Registration is already being processed")));
        if (!attempt.getIdempotencyKey().equals(idempotencyKey)) {
            throw new RegistrationConflictException("Email is already registered with another request");
        }
        if (!attempt.getRequestHash().equals(requestHash)
            || !attempt.getEmail().equals(email)
            || attempt.getRequestedRole() != role) {
            throw new RegistrationConflictException(
                "Idempotency-Key was already used for a different registration request");
        }
        return attempt;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RegistrationAttempt markUserCreated(UUID attemptId, UUID userId) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setUserId(userId);
        attempt.setStatus(RegistrationStatus.USER_CREATED);
        clearError(attempt);
        return repository.saveAndFlush(attempt);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markProfileCreated(UUID attemptId, UUID profileId) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setProfileId(profileId);
        attempt.setStatus(RegistrationStatus.PROFILE_CREATED);
        clearError(attempt);
        repository.saveAndFlush(attempt);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompleted(UUID attemptId) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setStatus(RegistrationStatus.COMPLETED);
        attempt.setCompletedAt(Instant.now());
        clearError(attempt);
        repository.saveAndFlush(attempt);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompensationPending(UUID attemptId, Throwable cause) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setStatus(RegistrationStatus.COMPENSATION_PENDING);
        recordError(attempt, "PROFILE_CREATION_FAILED", cause);
        attempt.setRetryCount(attempt.getRetryCount() + 1);
        repository.saveAndFlush(attempt);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompensated(UUID attemptId) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setStatus(RegistrationStatus.COMPENSATED);
        repository.saveAndFlush(attempt);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompensationFailed(UUID attemptId, Throwable cause) {
        RegistrationAttempt attempt = getForUpdate(attemptId);
        attempt.setStatus(RegistrationStatus.COMPENSATION_FAILED);
        recordError(attempt, "COMPENSATION_FAILED", cause);
        repository.saveAndFlush(attempt);
    }

    private RegistrationAttempt getForUpdate(UUID attemptId) {
        return repository.findById(attemptId)
            .orElseThrow(() -> new IllegalStateException("Registration attempt not found: " + attemptId));
    }

    private static void clearError(RegistrationAttempt attempt) {
        attempt.setErrorCode(null);
        attempt.setErrorMessage(null);
    }

    private static void recordError(RegistrationAttempt attempt, String code, Throwable cause) {
        attempt.setErrorCode(code);
        String message = cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
        attempt.setErrorMessage(message.substring(0, Math.min(message.length(), 1000)));
    }
}
