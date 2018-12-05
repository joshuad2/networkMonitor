#!/usr/bin/env sh

# set -eu

env

cd source-code/client
mvn package
pwd
ls -lah target/protoc-plugins
