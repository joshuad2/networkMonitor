#!/usr/bin/env bash

set -eu

pushd source-code/message
  mvn install
popd

pushd source-code/client
 mvn package
popd

pushd source-code/server
 mvn package
popd
