FROM maven:3.9.15-eclipse-temurin-26 AS builder

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests


FROM quay.io/keycloak/keycloak:26.6.4 AS keycloak

COPY --from=builder /workspace/target/auth-event-listener-spi-*.jar \
  /opt/keycloak/providers/auth-event-listener.jar

RUN /opt/keycloak/bin/kc.sh build

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
