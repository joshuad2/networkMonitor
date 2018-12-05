#!/usr/bin/env sh

set -eu

env

cd source-code/client
mvn compile
mvn test
ls -lah target/protoc-plugins
