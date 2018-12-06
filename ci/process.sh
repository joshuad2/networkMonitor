#!/usr/bin/env bash

set -eu

pushd source-code/message
  mvn install -Djdk.net.URLClassPath.disableClassPathURLCheck=true
popd

pushd source-code/client
 mvn package -Djdk.net.URLClassPath.disableClassPathURLCheck=true
popd

pushd source-code/server
 mvn package -Djdk.net.URLClassPath.disableClassPathURLCheck=true
popd
