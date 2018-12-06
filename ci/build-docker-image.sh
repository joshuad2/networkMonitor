#!/usr/bin/env bash

set -eu

docker build -t accenturepbg/maven:latest .
docker push accenturepbg/maven:latest
