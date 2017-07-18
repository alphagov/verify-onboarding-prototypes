# Prototype 0

This is the initial prototype developed as a strawman to test our assumptions around what is required for building a SP that meets RP needs.

Prototype 0 will be implemented as a Java application providing a minimal JSON interface without real SAML. It will simulate the ability to generate SAML AuthnRequests to Verify and process SAML Responses from Verify.

The `docs/adr` directory provides details on our current architectural decisions that we will test.

The `docs/diagrams` directory provides details diagrams describing:
* the suggested request flow
* the suggested response flow
* a service/component architecture

## Running the code

These projects are as of now (18-07-2017) frozen and shouldn't be used directly. The contents are separated into other repos:

### passport-verify

https://github.com/alphagov/passport-verify

### stub-rp

https://github.com/alphagov/passport-verify-stub-relying-party

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



