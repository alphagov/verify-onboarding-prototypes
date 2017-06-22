# Prototype 0

This is the initial prototype developed as a strawman to test our assumptions around what is required for building a SP that meets RP needs.

Prototype 0 will be implemented as a Java application providing a minimal JSON interface without real SAML. It will simulate the ability to generate SAML AuthnRequests to Verify and process SAML Responses from Verify.

The `docs/adr` directory provides details on our current architectural decisions that we will test.

The `docs/diagrams` directory provides details diagrams describing:
* the suggested request flow
* the suggested response flow
* a service/component architecture

## Running the code

There are four projects you need to build, and three that you need to run.

### passport-verify

This is a node library used to communicate with the verify-service-provider. You need to build it
and run `npm link` to create a symlink for the stub-rp:

```
cd passport-verify
npm install
./pre-commit.sh
npm link
```

### stub-rp

```
cd stub-rp
npm install
npm link passport-verify
./pre-commit.sh
./startup.sh
```

### verify-service-provider

```
cd verify-service-provider
./startup.sh
```

### stub-verify-hub

```
cd stub-verify-hub
./startup.sh
```

### Using the applications

Go to http://localhost:3200/verify/start

