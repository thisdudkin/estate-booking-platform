FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:25-jre

RUN addgroup --system app && adduser --system --ingroup app app

WORKDIR /application

COPY --from=builder /workspace/target/profile-service-*.jar application.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
