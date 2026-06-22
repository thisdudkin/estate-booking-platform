package io.petproject.estate.booking.platform.identity.provider.keycloak;

import io.petproject.estate.booking.platform.identity.dto.request.RegistrationRequest;
import io.petproject.estate.booking.platform.identity.exception.IdentityProviderConflictException;
import io.petproject.estate.booking.platform.identity.exception.IdentityProviderException;
import io.petproject.estate.booking.platform.identity.provider.IdentityProvider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeycloakTemplate implements IdentityProvider {

    private final RealmResource realm;

    public KeycloakTemplate(Keycloak keycloak, String realmName) {
        this.realm = keycloak.realm(realmName);
    }

    @Override
    public UUID createUser(
        RegistrationRequest request,
        String groupName,
        UUID registrationAttemptId
    ) {
        List<UserRepresentation> existingUsers = findExistingUsers(request.email());
        if (!existingUsers.isEmpty()) {
            return recoverOwnedUser(existingUsers, groupName, registrationAttemptId);
        }

        UserRepresentation user = toUserRepresentation(request, registrationAttemptId);
        try (Response response = realm.users().create(user)) {
            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new IdentityProviderConflictException("Identity already exists for the supplied email");
            }
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new IdentityProviderException("Identity provider rejected user creation", null);
            }

            UUID userId = UUID.fromString(CreatedResponseUtil.getCreatedId(response));
            try {
                realm.users().get(userId.toString()).joinGroup(findGroupId(groupName));
                return userId;
            } catch (RuntimeException exception) {
                disableAfterPartialCreation(userId, exception);
                throw translate("Failed to assign identity provider group", exception);
            }
        } catch (IdentityProviderException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw translate("Identity provider is unavailable", exception);
        }
    }

    private List<UserRepresentation> findExistingUsers(String email) {
        try {
            return realm.users().searchByEmail(email, true);
        } catch (RuntimeException exception) {
            throw translate("Identity provider is unavailable", exception);
        }
    }

    private UUID recoverOwnedUser(
        List<UserRepresentation> users,
        String groupName,
        UUID registrationAttemptId
    ) {
        UserRepresentation user = users.stream()
            .filter(candidate -> registrationAttemptId.toString().equals(firstAttribute(
                candidate, "registration_attempt_id")))
            .findFirst()
            .orElseThrow(() -> new IdentityProviderConflictException(
                "Identity already exists for the supplied email"));
        UUID userId = UUID.fromString(user.getId());
        realm.users().get(userId.toString()).joinGroup(findGroupId(groupName));
        return userId;
    }

    @Override
    public void disableUser(UUID userId) {
        try {
            var userResource = realm.users().get(userId.toString());
            UserRepresentation user = userResource.toRepresentation();
            user.setEnabled(false);
            userResource.update(user);
        } catch (RuntimeException exception) {
            throw translate("Failed to disable identity provider user", exception);
        }
    }

    private String findGroupId(String groupName) {
        List<GroupRepresentation> groups = realm.groups().groups(groupName, true, 0, 2, false);
        return groups.stream()
            .filter(group -> groupName.equals(group.getName()))
            .map(GroupRepresentation::getId)
            .findFirst()
            .orElseThrow(() -> new IdentityProviderException(
                "Identity provider group is not configured: " + groupName, null));
    }

    private void disableAfterPartialCreation(UUID userId, RuntimeException originalException) {
        try {
            disableUser(userId);
        } catch (RuntimeException compensationException) {
            originalException.addSuppressed(compensationException);
        }
    }

    private static UserRepresentation toUserRepresentation(
        RegistrationRequest request,
        UUID registrationAttemptId
    ) {
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setTemporary(false);
        password.setValue(request.password());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.email());
        user.setEmail(request.email());
        user.setFirstName(request.givenName());
        user.setLastName(request.familyName());
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setCredentials(List.of(password));
        user.setAttributes(Map.of(
            "registration_attempt_id", List.of(registrationAttemptId.toString())
        ));
        return user;
    }

    private static String firstAttribute(UserRepresentation user, String name) {
        List<String> values = user.getAttributes() == null ? null : user.getAttributes().get(name);
        return values == null || values.isEmpty() ? null : values.getFirst();
    }

    private static IdentityProviderException translate(String message, RuntimeException exception) {
        if (exception instanceof WebApplicationException webException
            && webException.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            return new IdentityProviderConflictException("Identity already exists for the supplied email");
        }
        return new IdentityProviderException(message, exception);
    }
}
