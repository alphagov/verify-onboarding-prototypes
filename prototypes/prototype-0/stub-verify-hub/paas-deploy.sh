#!/usr/bin/env bash

./gradlew distZip

cf push stub-verify-hub -p build/distributions/stub-verify-hub.zip
