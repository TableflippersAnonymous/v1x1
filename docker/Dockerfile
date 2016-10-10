FROM openjdk:8-jre-alpine
MAINTAINER Josh Johnson <snofox@snofox.net>
WORKDIR /v1x1
ENTRYPOINT ["/usr/bin/java", "-jar"]
CMD ["module.jar"]
ONBUILD ADD *.jar module.jar
