package io.petproject.estate.booking.platform.keycloak.spi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.time.Instant;
import java.util.UUID;

final class RegistrationEventListenerProvider implements EventListenerProvider {
    private static final String REQUESTED_ROLE_ATTRIBUTE = "requested_role";
    private static final String TENANT_ROLE = "TENANT";
    private static final String LANDLORD_ROLE = "LANDLORD";
    private static final String TENANTS_GROUP = "tenants";
    private static final String LANDLORDS_GROUP = "landlords";

    private final KeycloakSession session;
    private final KafkaProducer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper;
    private final String topic;

    RegistrationEventListenerProvider(
            KeycloakSession session,
            KafkaProducer<String, String> kafkaProducer,
            String topic
    ) {
        this.session = session;
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() != EventType.REGISTER) {
            return;
        }
        RealmModel realm = session.realms().getRealm(event.getRealmId());
        if (realm == null) {
            return;
        }
        UserModel user = session.users().getUserById(realm, event.getUserId());
        if (user == null) {
            return;
        }
        String role = user.getFirstAttribute(REQUESTED_ROLE_ATTRIBUTE);
        String groupName = groupNameForRole(role);
        GroupModel group = findTopLevelGroupByName(realm, groupName);
        if (!user.isMemberOf(group)) {
            user.joinGroup(group);
        }
        user.setEmailVerified(true);

        try {
            publishUserRegistered(user, role);
        } catch (UserRegisteredPublicationException e) {
            removeUserAfterPublicationFailure(realm, user);
            throw e;
        }
    }

    private void publishUserRegistered(UserModel user, String role) {
        try {
            UserRegisteredEvent event = new UserRegisteredEvent(
                    UUID.randomUUID().toString(),
                    "UserRegistered",
                    Instant.now(),
                    "keycloak-registration-spi",
                    new UserRegisteredPayload(
                            user.getId(),
                            user.getEmail(),
                            displayName(user),
                            role,
                            user.isEmailVerified()
                    )
            );
            String json = objectMapper.writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    user.getId(),
                    json
            );
            kafkaProducer.send(record).get();
        } catch (Exception e) {
            throw new UserRegisteredPublicationException("Failed to publish UserRegistered event", e);
        }
    }

    private void removeUserAfterPublicationFailure(RealmModel realm, UserModel user) {
        boolean removed = session.users().removeUser(realm, user);
        if (!removed) {
            throw new IllegalStateException("Failed to remove user after UserRegistered publication failure: " + user.getId());
        }
    }

    private static String displayName(UserModel user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (isBlank(firstName) && isBlank(lastName)) {
            return user.getUsername();
        }
        return ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
    }

    @Override
    public void close() {
    }

    private String groupNameForRole(String role) {
        return switch (role) {
            case TENANT_ROLE -> TENANTS_GROUP;
            case LANDLORD_ROLE -> LANDLORDS_GROUP;
            default -> throw new IllegalStateException("Invalid requested_role: " + role);
        };
    }

    private GroupModel findTopLevelGroupByName(RealmModel realm, String groupName) {
        return session.groups()
                .getTopLevelGroupsStream(realm)
                .filter(group -> groupName.equals(group.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Required group does not exist: /" + groupName));
    }

    private static final class UserRegisteredPublicationException extends RuntimeException {
        private UserRegisteredPublicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
