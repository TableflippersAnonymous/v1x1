#!/usr/bin/env bash
getBoolAnswer() {
    while true; do
        read -p "$@ [y/n] " -n1
        printf "\n" >&2
        REPLY=$(echo $REPLY|tr [A-Z] [a-z])
        if [ $REPLY == "y" ] || [ $REPLY == "n" ]; then
            echo $REPLY
            return
        fi
    done
}
APP_ENV=$1
if [ x${APP_ENV} == 'x' ]; then
  echo "Usage: ./build.sh <dev|prod>"
  exit 1
fi
MODULES="v1x1-base"
cd $(dirname $0)
echo "Finding modules..."
while read module; do
  MODULES="$MODULES v1x1-$module"
done < <(find twitchbot-modules -mindepth 2 -iname "twitchbot-modules-*" -type d -printf '%f\n'|cut -f4- -d- --)
echo "Clearing out old Docker containers..."
docker-compose down
echo "Clearing out old Docker images..."
docker rmi ${MODULES}

echo "Rebuilding..."
MAVEN_OPTS=-Djava.io.tmpdir=${HOME}/tmp mvn clean install -Denvironmnent=$APP_ENV

if [ $(getBoolAnswer "Run new build?") == "y" ]; then
    docker-compose up -d cass1 redis zoo1
    echo "Waiting for core services to start..."
    sleep 45
    docker-compose up -d
fi
