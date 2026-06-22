package io.petproject.estate.booking.platform.identity.provider;

import io.petproject.estate.booking.platform.identity.dto.request.RegistrationRequest;

import java.util.UUID;

public interface IdentityProvider {

    UUID createUser(RegistrationRequest request, String groupName, UUID registrationAttemptId);

    void disableUser(UUID userId);
}
