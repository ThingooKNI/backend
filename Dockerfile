FROM openjdk:15-alpine as builder
WORKDIR /app

COPY gradle gradle
COPY src src
COPY build.gradle.kts .
COPY gradlew .
COPY settings.gradle.kts .
COPY system.properties .

RUN ./gradlew build -x test


FROM openjdk:11-jre-slim-buster
VOLUME /tmp
ENV JAVA_OPTIONS="-Dspring.profiles.active=production"
COPY --from=builder /app/build/libs/thingoo-backend.jar /etc/spring/thingoo-backend.jar
ENTRYPOINT java $JAVA_OPTIONS -jar /etc/spring/thingoo-backend.jar --spring.config.location=optional:file:/etc/spring/config/
