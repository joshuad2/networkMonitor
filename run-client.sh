#!/usr/bin/env bash

pushd client
  mvn exec:java -Dexec.mainClass=org.networkMonitor.client.Client
popd
