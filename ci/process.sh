#!/usr/bin/env bash

set -eu

pushd source-code/message
  mvn install -DforkCount=0
popd

pushd source-code/client
 mvn package -DforkCount=0
popd

pushd source-code/server
 mvn package -DforkCount=0
popd
