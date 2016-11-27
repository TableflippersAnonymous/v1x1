#!/bin/bash

VERSION="$(cat /tmp/v1x1-version.txt)"
rm -f /tmp/v1x1-version.txt

source /tmp/v1x1-login.sh
rm -f /tmp/v1x1-login.sh

for service_id in $(docker service ls -f 'label=deploy-group=v1x1' -q)
do
	service_image="$(docker service inspect -f '{{.Spec.TaskTemplate.ContainerSpec.Image}}' "${service_id}")"
	service_image_registry="$(echo "${service_image}" | awk -F/ '{print $1}')"
	service_image_repo="$(echo "${service_image}" | awk -F/ '{print $2}' | awk -F: '{print $1}')"
	docker service update --with-registry-auth --image "${service_image_registry}/${service_image_repo}:${VERSION}" "${service_id}"
done
