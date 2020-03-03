# build stage
FROM maven:3-jdk-11 as builder
RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests=true

# create Image stage
#FROM openjdk:8-jre-alpine
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim

VOLUME /tmp

COPY --from=builder  /usr/src/app/target/emr-notifier-*.jar ./emr-notifier.jar

RUN sh -c 'touch ./emr-notifier.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=DEV","-Djava.security.egd=file:/dev/./urandom","-jar","./emr-notifier.jar"]