#!/usr/bin/env bash
REGISTRY="registry.tblflp.zone:5443"
MODULES="v1x1-base"
MODULECOUNT=0
cd $(dirname $0)
echo -n "Finding modules... "
while read MODULE; do
  MODULES="$MODULES $MODULE"
  MODULECOUNT=$(($MODULECOUNT + 1))
done < <(docker images 'v1x1-*:latest'|grep -vF 'v1x1-base'|egrep -v '^REPOSITORY'|awk '{print $1}')
echo "Found $MODULECOUNT"
echo -n "Getting current revision... "
VERSION=$(git rev-parse --short HEAD)
echo $VERSION
echo "Uploading..."
for MODULE in $MODULES; do
    docker tag ${MODULE}:latest ${REGISTRY}/${MODULE}:${VERSION}
    docker push ${REGISTRY}/${MODULE}:${VERSION}
done

if [[ $CIRCLE_BRANCH == "master" ]]; then
    (
        cd v1x1-web/dist
        aws s3 cp --content-type text/css *.css s3://v1x1-web/
        aws s3 cp --content-type application/javascript *.js s3://v1x1-web/
        aws s3 cp --content-type text/html --cache-control max-age=3600 *.html s3://v1x1-web/
        aws cloudfront create-invalidation --distribution-id E1ES20CHGVRXZB --paths '/index.html' '/*.html'
    )
fi