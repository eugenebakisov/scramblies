# syntax = docker/dockerfile:1.2
FROM node:12 AS node
ENV NODE_ENV=production
COPY package*.json ./
RUN npm install --production

FROM clojure:openjdk-17 AS build
WORKDIR /

COPY --from=node . /
COPY . /
ENV NODE_ENV=production
RUN clj -Sforce -T:build all

FROM azul/zulu-openjdk-alpine:17

COPY --from=build /target/scramblies-standalone.jar /scramblies/scramblies-standalone.jar

EXPOSE 3000

ENTRYPOINT exec java $JAVA_OPTS -jar /scramblies/scramblies-standalone.jar
