import * as assert from 'assert'
import { PassportVerifyStrategy, USER_NOT_ACCEPTED_ERROR, AUTHENTICATION_FAILED_ERROR, VerifyServiceProviderError } from '../lib/passport-verify-strategy'
import * as td from 'testdouble'

describe('The passport-verify strategy', function () {

  const exampleSaml = {
    body: {
      SAMLResponse: 'some-saml-response',
      RelayState: 'some-relay-state'
    }
  }
  const exampleAuthnRequestResponse = {
    samlRequest: 'some-saml-req',
    secureToken: 'some-secure-token',
    location: 'http://hub-sso-uri'
  }
  const exampleTranslatedResponse = {
    scenario: 'SUCCESS',
    pid: 'some-pid',
    levelOfAssurance: 'LEVEL_2',
    attributes: {}
  }
  const exampleUser = {
    id: 1
  }

  function createStrategy () {
    return new PassportVerifyStrategy(
      () => Promise.resolve(exampleAuthnRequestResponse),
      () => Promise.resolve(exampleTranslatedResponse),
      (exampleTranslatedResponse) => exampleUser
    )
  }

  it('should render a SAML AuthnRequest form', function () {
    const send = td.function()
    const request: any = { res: { send } }
    return createStrategy().authenticate(request).then(() => {
      td.verify(send(td.matchers.contains(/some-saml-req/)))
      td.verify(send(td.matchers.contains(/http:\/\/hub-sso-uri/)))
    })
  })

  it('should convert a successful SAML AuthnResponse to the application user object', function () {
    const strategy = createStrategy() as any

    // Mimicking passport's attaching of its success method to the Strategy instance
    strategy.success = td.function()

    return strategy.authenticate(exampleSaml).then(() => {
      td.verify(strategy.success(td.matchers.contains(exampleUser), td.matchers.anything()))
    })
  })

  it('should fail if the application does not accept the user', function () {
    const strategy = new PassportVerifyStrategy(
      () => Promise.resolve(exampleAuthnRequestResponse),
      () => Promise.resolve(exampleTranslatedResponse),
      (exampleTranslatedResponse) => null
    ) as any

    // Mimicking passport's attaching of its fail method to the Strategy instance
    strategy.fail = td.function()

    return strategy.authenticate(exampleSaml).then(() => {
      td.verify(strategy.fail(USER_NOT_ACCEPTED_ERROR))
    })
  })

  it('should fail if the response is 401 from verify-service-provider', () => {
    const strategy = new PassportVerifyStrategy(
      () => Promise.resolve(exampleAuthnRequestResponse),
      () => Promise.reject(new VerifyServiceProviderError('AUTHENTICATION_FAILED', '', 401)),
      (exampleTranslatedResponse) => null
    ) as any

    // Mimicking passport's attaching of its fail method to the Strategy instance
    strategy.fail = td.function()

    return strategy.authenticate(exampleSaml).catch(() => {
      td.verify(strategy.fail(AUTHENTICATION_FAILED_ERROR, 401))
    })
  })

})
