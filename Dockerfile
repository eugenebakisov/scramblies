# syntax = docker/dockerfile:1.2
FROM azul/zulu-openjdk-alpine:17

COPY ./target/scramblies-standalone.jar /scramblies/scramblies-standalone.jar

EXPOSE 3000

ENTRYPOINT exec java $JAVA_OPTS -jar /scramblies/scramblies-standalone.jar
