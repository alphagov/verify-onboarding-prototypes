import * as assert from 'assert'
import { PassportVerifyStrategy, USER_NOT_ACCEPTED_ERROR } from '../lib/passport-verify-strategy'
import * as td from 'testdouble'

describe('The passport-verify strategy', function () {

  const exampleAuthnRequestResponse = {
    samlRequest: 'some-saml-req',
    secureToken: 'some-secure-token',
    location: 'http://hub-sso-uri'
  }
  const exampleTranslatedResponse = {
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

    const samlSuccessRequest = {
      body: {
        SAMLResponse: 'some-saml-response',
        RelayState: 'some-relay-state'
      }
    }

    return strategy.authenticate(samlSuccessRequest).then(() => {
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

    const samlSuccessRequest = {
      body: {
        SAMLResponse: 'some-saml-response',
        RelayState: 'some-relay-state'
      }
    }

    return strategy.authenticate(samlSuccessRequest).then(() => {
      td.verify(strategy.fail(USER_NOT_ACCEPTED_ERROR))
    })
  })

})
