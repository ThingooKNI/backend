FROM openjdk:11-jre-slim-buster
VOLUME /tmp
ENV JAVA_OPTIONS="-Dspring.profiles.active=production"
COPY build/libs/thingoo-backend.jar /etc/spring/thingoo-backend.jar
ENTRYPOINT java $JAVA_OPTIONS -jar /etc/spring/thingoo-backend.jar --spring.config.location=optional:file:/etc/spring/config/,classpath:/
