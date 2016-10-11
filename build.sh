#!/bin/bash
MODULES="v1x1-base"
cd $(dirname $0)
echo "Finding modules..."
while read module; do
  MODULES="$MODULES v1x1-$module"
done < <(find twitchbot-modules -mindepth 2 -iname "twitchbot-modules-*" -type d -printf '%f\n'|cut -f4- -d- --)
echo "Clearing out old Docker images..."
docker rmi ${MODULES}

echo "Rebuilding..."
MAVEN_OPTS=-Djava.io.tmpdir=${HOME}/tmp mvn clean install
