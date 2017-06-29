#!/usr/bin/env bash
set -e
CURRENT_DIR=$PWD
function cleanup {
  cd "$CURRENT_DIR"
}
trap cleanup EXIT
cd "$(dirname "$0")"

cd prototypes/prototype-0

for APP_NAME in stub-rp
do
  ./$APP_NAME/pre-commit.sh
done
