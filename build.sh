#!/bin/bash
# ./server/pom-debloated.xml
mvn -f ./server/opencbs-server clean package -DBUILD_VERSION=1.0.1 -Denv.NODE_OPTIONS=--openssl-legacy-provider -am -DskipTests=true -DskipITs=true -T1C

