#!/usr/bin/env bash
REGISTRY="registry.tblflp.zone:5443"
MODULES="v1x1-base"
MODULECOUNT=0
cd $(dirname $0)
echo -n "Finding modules... "
while read MODULE; do
  MODULES="$MODULES $MODULE"
  MODULECOUNT=$(($MODULECOUNT + 1))
done < <(docker image ls --format '{{.Repository}}' 'v1x1-*:latest')
echo "Found $MODULECOUNT"
echo -n "Getting current revision... "
VERSION=$(git rev-parse --short HEAD)
echo $VERSION
echo "Uploading..."
for MODULE in $MODULES; do
    docker tag ${MODULE}:latest ${REGISTRY}/${MODULE}:${VERSION}
    docker push ${REGISTRY}/${MODULE}:${VERSION}
done
