FROM openjdk:8-jre-alpine
MAINTAINER Josh Johnson <snofox@snofox.net>
USER 31337
WORKDIR /v1x1
EXPOSE 5005
ENTRYPOINT ["/usr/bin/java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar"]
CMD ["module.jar", "config.yml"]
ARG APP_ENV=dev
COPY ${APP_ENV}.yml config.yml
ONBUILD COPY *.jar module.jar
