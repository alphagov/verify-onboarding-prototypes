#!/usr/bin/env bash

cd "$(dirname $0)/.."
root_directory="$(pwd)"

echo 'Starting the applications. Logs will be placed in acceptance-tests/logs.'
echo ''
echo 'Note if the applications are already running this script will skip them.'
echo 'Also note this script will not stop the applications when it is done.'
echo ''

for d in verify-service-provider stub-verify-hub stub-rp
do
  cd "$root_directory/$d"
  logs_dir="$root_directory/acceptance-tests/logs"
  ./startup.sh > "$logs_dir/$d.stdout.log" 2> "$logs_dir/$d.stderr.log" &
done

echo 'Waiting 10 seconds to give the applications a chance to start. Don'"'"'t hate me because I'"'"'m hacky...'
echo ''
sleep 10

# Initialise the stub-verify-hub with our entityId
curl http://localhost:50410/configure-rp --data '{"rpEntityId": "some-saml","assertionConsumerServiceUrl": "http://localhost:3200/verify/response"}' -H 'Content-Type: application/json'

echo 'Running the tests'

# Run the tests
cd "$root_directory/acceptance-tests"
if ! ./gradlew check
then
  >&2 echo ''
  >&2 echo 'Tests failed :('
  exit 1
fi

