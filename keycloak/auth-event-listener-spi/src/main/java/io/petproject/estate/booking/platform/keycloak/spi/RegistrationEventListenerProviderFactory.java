package io.petproject.estate.booking.platform.keycloak.spi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.Properties;

public final class RegistrationEventListenerProviderFactory implements EventListenerProviderFactory {
    private static final String PROVIDER_ID = "registration-event-listener";

    private String bootstrapServers;
    private String topic;
    private KafkaProducer<String, String> kafkaProducer;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new RegistrationEventListenerProvider(session, kafkaProducer, topic);
    }

    @Override
    public void init(Config.Scope config) {
        this.bootstrapServers = config.get("bootstrapServers", "kafka-1:29092,kafka-2:29092,kafka-3:29092");
        this.topic = config.get("topic", "identity.user-registered.v1");

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("acks", "all");
        properties.put("enable.idempotence", "true");
        properties.put("delivery.timeout.ms", "10000");
        properties.put("request.timeout.ms", "5000");
        properties.put("max.block.ms", "5000");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
        if (kafkaProducer != null) {
            kafkaProducer.close();
        }
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

}
