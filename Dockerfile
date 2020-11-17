FROM openjdk:11
VOLUME /tmp
ENV JAVA_OPTIONS="-Dspring.profiles.active=production"
ADD build/libs/thingoo-backend.jar /etc/spring/thingoo-backend.jar
ENTRYPOINT java $JAVA_OPTIONS -jar /etc/spring/thingoo-backend.jar
