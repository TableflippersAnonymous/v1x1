dependencies: mvn-dep npm-dep pip-dep docker-dep kube-dep
	aws configure set preview.cloudfront true

mvn-dep:
	mvn --fail-never dependency:go-offline || true
	(cd v1x1-common; mvn --fail-never dependency:go-offline) || true
	(cd v1x1-modules/v1x1-modules-core/v1x1-modules-core-api; mvn --fail-never dependency:go-offline) || true

npm-dep:
	(cd v1x1-web && npm install)

pip-dep:
	pip install awscli

docker-dep:
	docker pull openjdk:8-jre-alpine

kube-dep:
    mkdir -p ~/bin
    wget -O ~/bin/kubectl https://storage.googleapis.com/kubernetes-release/release/v1.6.0/bin/linux/amd64/kubectl
    chmod +x ~/bin/kubectl
    wget -O ~/kube-ca.crt http://pki.tblflp.zone/Tableflippers-Anonymous-Infrastructure-CA.crt
    ~/bin/kubectl config set-cluster tblflp --server=https://k8s-master.tblflp.zone --certificate-authority=~/kube-ca.crt
    ~/bin/kubectl config set-credentials tblflp "--username=$KUBE_USERNAME" "--password=$KUBE_PASSWORD"
    ~/bin/kubectl config set-context tblflp --cluster=tblflp --user=tblflp
    ~/bin/kubectl config use-context tblflp
    ~/bin/kubectl cluster-info

test: mvn-test npm-build

mvn-test: docker-login
	mvn -T16 integration-test -Denvironment=prod

npm-build:
	(cd v1x1-web && npm run build)

cert-setup:
	sudo mkdir -p /etc/docker/certs.d/registry.tblflp.zone:5443
	sudo wget -O /etc/docker/certs.d/registry.tblflp.zone:5443/ca.crt http://pki.tblflp.zone/Tableflippers-Anonymous-Root-CA.pem
	cat /etc/ssl/certs/ca-certificates.crt | sudo tee -a /etc/docker/certs.d/registry.tblflp.zone:5443/ca.crt

docker-login: cert-setup
	while true; do docker login -e ${DOCKER_EMAIL} -u ${DOCKER_USER} -p ${DOCKER_PASS} registry.tblflp.zone:5443 && break; sleep 3; done
	echo '#!/bin/bash' > codedeploy/v1x1-login.sh
	echo "docker login -e ${DOCKER_EMAIL} -u ${DOCKER_USER} -p ${DOCKER_PASS} registry.tblflp.zone:5443" >> codedeploy/v1x1-login.sh
	chmod +x codedeploy/v1x1-login.sh

deploy: upload
	./update_kube.sh

upload:
    ./upload.sh
