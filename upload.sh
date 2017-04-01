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
echo "Building plan ..."
echo > Makefile.upload
MAKEFILE_TARGETS=""
for MODULE in $MODULES; do
    MAKEFILE_TARGETS="${MAKEFILE_TARGETS} ${MODULE}"
    echo "${MODULE}:" >> Makefile.upload
    echo -e "\tdocker tag ${MODULE}:latest ${REGISTRY}/${MODULE}:${VERSION}" >> Makefile.upload
    echo -e "\twhile true; do docker push ${REGISTRY}/${MODULE}:${VERSION} && break; sleep 3; done" >> Makefile.upload
done

if [[ $CIRCLE_BRANCH == "master" ]]; then
    for file in v1x1-web/dist/*.css ; do
        MAKEFILE_TARGETS="${MAKEFILE_TARGETS} upload-${file}"
        echo "upload-${file}:" >> Makefile.upload
        echo -e "\taws s3 cp --content-type text/css ${file} s3://v1x1-web/" >> Makefile.upload
    done
    for file in v1x1-web/dist/*.js ; do
        MAKEFILE_TARGETS="${MAKEFILE_TARGETS} upload-${file}"
        echo "upload-${file}:" >> Makefile.upload
        echo -e "\taws s3 cp --content-type application/javascript ${file} s3://v1x1-web/" >> Makefile.upload
    done
    for file in v1x1-web/dist/*.html ; do
        MAKEFILE_TARGETS="${MAKEFILE_TARGETS} upload-${file}"
        echo "upload-${file}:" >> Makefile.upload
        echo -e "\taws s3 cp --content-type text/html --cache-control max-age=3600 ${file} s3://v1x1-web/" >> Makefile.upload
    done
fi

echo "upload: ${MAKEFILE_TARGETS}" >> Makefile.upload

echo "Uploading..."
make -j -f Makefile.upload upload

rm -f Makefile.upload

if [[ $CIRCLE_BRANCH == "master" ]]; then
    echo "Invalidating Cloudfront..."
    aws cloudfront create-invalidation --distribution-id E1ES20CHGVRXZB --paths '/index.html' '/*.html' '/'
fi
