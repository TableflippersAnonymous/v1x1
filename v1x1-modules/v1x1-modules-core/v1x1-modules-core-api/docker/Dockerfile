FROM v1x1-base
USER 0
RUN apk update && apk add openssl
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
RUN mkdir -p /tmp
RUN chmod 1777 /tmp
USER 31337
ENTRYPOINT ["/entrypoint.sh"]
