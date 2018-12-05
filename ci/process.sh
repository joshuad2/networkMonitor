#!/usr/bin/env bash

set -eu

env

pushd source-code/client
  mvn package
popd
