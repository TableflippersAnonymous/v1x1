FROM openjdk:8-jre-alpine
MAINTAINER Josh Johnson <snofox@snofox.net>
USER 31337
WORKDIR /v1x1
ENTRYPOINT ["/usr/bin/java", "-jar"]
CMD ["module.jar", "config.yml"]
ARG APP_ENV=dev
COPY ${APP_ENV}.yml config.yml
ONBUILD ADD *.jar module.jar
