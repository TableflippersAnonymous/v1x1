dependencies: mvn-dep npm-dep pip-dep docker-dep
	aws configure set preview.cloudfront true

mvn-dep:
	mvn --fail-never dependency:go-offline || true

npm-dep:
	(cd v1x1-web && npm install)

pip-dep:
	pip install awscli

docker-dep:
	docker pull openjdk:8-jre-alpine

test: mvn-test npm-build
	./upload.sh
	codedeploy/prepare.sh

mvn-test: docker-login
	mvn -T16 integration-test -Denvironment=prod

npm-build:
	(cd v1x1-web && npm run build)

cert-setup:
	sudo mkdir -p /etc/docker/certs.d/registry.tblflp.zone:5443
	sudo wget -O /etc/docker/certs.d/registry.tblflp.zone:5443/ca.crt http://pki.tblflp.zone/Tableflippers-Anonymous-Root-CA.pem
	cat /etc/ssl/certs/ca-certificates.crt | sudo tee -a /etc/docker/certs.d/registry.tblflp.zone:5443/ca.crt

docker-login: cert-setup
	while true; do docker login -e ${DOCKER_EMAIL} -u ${DOCKER_USER} -p ${DOCKER_PASS} registry.tblflp.zone:5443; sleep 3; done
	echo '#!/bin/bash' > codedeploy/v1x1-login.sh
	echo "docker login -e ${DOCKER_EMAIL} -u ${DOCKER_USER} -p ${DOCKER_PASS} registry.tblflp.zone:5443" >> codedeploy/v1x1-login.sh
	chmod +x codedeploy/v1x1-login.sh

