Stub Verify Hub
===============

Stub Verify Hub provides various base64 encoded responses for prototype-0 of Verify Service Provider to consume.
https://github.com/alphagov/verify-service-provider/tree/master/prototypes/prototype-0/verify-service-provider

This Service is hosted continuosly deployed on PAAS:
https://stub-verify-hub-proto-demo.cloudapps.digital/SAML2/SSO

The service can be used via Stub RP at https://stub-rp-proto-demo.cloudapps.digital/



Scenarios
---------

__Success Match__
```
{
  "levelOfAssurance": "LEVEL_2",
  "pid": "pid",
  "scenario": "SUCCESS_MATCH"
}
```

__Account Creation__
```
{
  "scenario": "ACCOUNT_CREATION",
  "pid": "new-user-pid",
  "attributes": {
    "surnameVerified": true,
    "firstName": "Some first name",
    "address": {
      "internationalPostCode": "some international post code",
      "uprn": "some uprn",
      "verified": true,
      "postCode": "some post code",
      "lines": [
        "Address line 1",
        "Address line 2",
        "Address line 3"
      ]
    },
    "surname": "some surname ",
    "middleName": "some middle name",
    "dateOfBirth": "1990-01-01",
    "cycle3": "some cycle 3",
    "middleNameVerified": true,
    "dateOfBirthVerified": true,
    "firstNameVerified": true
  },
  "levelOfAssurance": "LEVEL_2"
}
```

__Authentication Failed__
```
{
  "scenario": "AUTHENTICATION_FAILED"
}
```

__No Match__
```
{
  "scenario": "NO_MATCH"
}
```

__Cancellation__
```
{
  "scenario": "CANCELLATION"
}
```

__Request Error__
```
{
  "scenario": "REQUEST_ERROR"
}
```

__Internal Server Error__
```
{
  "scenario": "INTERNAL_SERVER_ERROR"
}
```


Development
-----------

__Test__
```
./pre-commit.sh
```

__Start__
```
./startup.sh
```

Remember to run acceptance tests after changes https://github.com/alphagov/verify-service-provider/tree/master/prototypes/prototype-0/acceptance-tests
