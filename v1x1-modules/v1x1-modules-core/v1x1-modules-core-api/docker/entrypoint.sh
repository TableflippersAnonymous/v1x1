#!/bin/sh
openssl pkcs12 -export -in /run/secrets/api-backend.v1x1.tv.crt -inkey /run/secrets/api-backend.v1x1.tv.key -out /tmp/pkcs.p12 -name cert -password pass:notsecure
keytool -deststorepass notsecure -importkeystore -destkeypass notsecure -destkeystore /tmp/keystore.jks -srckeystore /tmp/pkcs.p12 -srcstoretype \
  PKCS12 -srcstorepass notsecure -alias cert
/usr/bin/java -jar module.jar config.yml