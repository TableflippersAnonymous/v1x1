#!/usr/bin/env bash
TYPE=channel
APP_ENV=dev
NAME=$1
MAVEN_OPTS=-Djava.io.tmpdir=${HOME}/tmp
if [ $# -eq 0 ]; then
    echo "What module?"
    exit 1
fi
if [ $# -eq 2 ]; then
    TYPE=$2
fi
cd $(dirname $0)
docker-compose stop $NAME
docker-compose rm -f $NAME
docker rmi v1x1-${NAME}:latest
mvn clean install -pl v1x1-common
mvn clean install -pl v1x1-modules/v1x1-modules-${TYPE}/v1x1-modules-${TYPE}-${NAME} -Denvironmnent=$APP_ENV
docker-compose create $NAME
docker-compose start $NAME
