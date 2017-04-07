#!/bin/bash

VERSION=$(git rev-parse --short HEAD)

for deployment in $(~/bin/kubectl -n v1x1 get deployments --selector=deploy-group==v1x1 --template='{{range .items}}{{.metadata.name}} {{end}}')
do
    for container in $(~/bin/kubectl -n v1x1 get deployments "${deployment}" --template='{{range .spec.template.spec.containers}}{{.name}}={{.image}} {{end}}')
    do
        container_name="$(echo "${container}" | awk -F= '{print $1}')"
        container_image="$(echo "${container}" | awk -F= '{print $2}')"
        container_image_registry="$(echo "${container_image}" | awk -F/ '{print $1}')"
        container_image_repo="$(echo "${container_image}" | awk -F/ '{print $2}' | awk -F: '{print $1}')"
        ~/bin/kubectl -n v1x1 set image "deployment/${deployment}" "${container_name}=${container_image_registry}/${container_image_repo}:${VERSION}"
    done
done
