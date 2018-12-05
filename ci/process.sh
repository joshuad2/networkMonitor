#!/usr/bin/env sh

set -eu

cd source-code/message
mvn install
cd ../client
mvn package
