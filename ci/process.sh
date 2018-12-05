#!/usr/bin/env sh

set -eu

env

pushd source-code/client
  mvn package
popd
