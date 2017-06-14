#!/usr/bin/env bash

# something about using gradles distZip to:
./gradlew distZip

tar -xf build/distributions/stub-verify-hub.zip -C build/distributions/

./build/distributions/stub-verify-hub/bin/stub-verify-hub server ./configuration/stub-verify-hub.yml
