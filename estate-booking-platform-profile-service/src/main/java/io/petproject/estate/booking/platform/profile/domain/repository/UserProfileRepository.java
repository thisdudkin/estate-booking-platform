package io.petproject.estate.booking.platform.profile.domain.repository;

import io.petproject.estate.booking.platform.profile.domain.model.user.EmailAddress;
import io.petproject.estate.booking.platform.profile.domain.model.user.KeycloakUserId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserId;
import io.petproject.estate.booking.platform.profile.domain.model.user.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findById(UserId id);

    Optional<UserProfile> findByKeycloakUserId(KeycloakUserId keycloakUserId);

    boolean existsById(UserId id);

    boolean existsByKeycloakUserId(KeycloakUserId keycloakUserId);

    boolean existsByEmailAddress(EmailAddress emailAddress);

    boolean existsByEmailAddressExcludingUserId(EmailAddress emailAddress, UserId excludedUserId);

    void save(UserProfile userProfile);

    void softDelete(UserId id);

    void hardDelete(UserId id);
}
