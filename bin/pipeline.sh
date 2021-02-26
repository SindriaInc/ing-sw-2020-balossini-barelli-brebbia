#!/usr/bin/env bash

BITBUCKET_TAG=$1

# Build
cd src
mvn clean

# Test
ls -la
cd ..

# Build Image
bash build.sh sindriainc/ing-sw-2020-balossini-barelli-brebbia ${BITBUCKET_TAG}

# Push Image
docker push sindriainc/ing-sw-2020-balossini-barelli-brebbia:${BITBUCKET_TAG}
docker push sindriainc/ing-sw-2020-balossini-barelli-brebbia:latest
