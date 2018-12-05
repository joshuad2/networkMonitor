#!/usr/bin/env bash

set -eu

pushd source-code/client
  mvn package
popd
