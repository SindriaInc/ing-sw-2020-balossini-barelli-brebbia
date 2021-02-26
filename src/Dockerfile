FROM sindriainc/openjdk:1.1.0-14

WORKDIR /var/www/app

ARG TAG_VERSION
ARG HOST_USER_UID
ARG TIMEZONE

LABEL \
	name="ing-sw-2020-balossini-barelli-brebbia" \
	image="sindriainc/ing-sw-2020-balossini-barelli-brebbia" \
	tag="${TAG_VERSION}${TAG_SUFFIX}" \
	vendor="sindria"

ENV DEBIAN_FRONTEND="noninteractive"
ENV TZ=${TIMEZONE}
ENV SINDRIA_USER="sindria"
ENV SINDRIA_USER_HOME="/home/sindria"
ENV SANTORINI_SERVER_PORT="25565"

# Install application
COPY ./deliverables/jar/ /var/www/app

# Setting Timezone and Fixing permission
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    chmod -R 770 /var/www/app && \
    chown -R ${SINDRIA_USER}:${SINDRIA_USER} /var/www/app

COPY cmd.sh /cmd.sh
CMD ["/bin/bash", "/cmd.sh"]
