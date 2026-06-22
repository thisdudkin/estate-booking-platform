package io.petproject.estate.booking.platform.identity.repository;

import io.petproject.estate.booking.platform.identity.entity.RegistrationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegistrationAttemptRepository extends JpaRepository<RegistrationAttempt, UUID> {

    Optional<RegistrationAttempt> findByIdempotencyKey(String idempotencyKey);

    Optional<RegistrationAttempt> findByEmail(String email);
}
