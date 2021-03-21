FROM openjdk:17-slim

RUN mkdir /etc/spring && useradd spring && chown -R spring /etc/spring

USER spring

VOLUME /tmp

EXPOSE 8080

COPY build/libs/thingoo-backend.jar /etc/spring/thingoo-backend.jar

ENTRYPOINT java -jar /etc/spring/thingoo-backend.jar --spring.config.location=file:/etc/spring/config/application.yml
