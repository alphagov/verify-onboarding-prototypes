import * as assert from 'assert'
import PassportVerifyStrategy from '../lib/passport-verify-strategy'
import * as td from 'testdouble'

describe('The passport-verify strategy', function () {

  it('should render a SAML AuthnRequest form', function () {
    const strategy = new PassportVerifyStrategy()
    const send = td.function()
    const request: any = {
      res: {
        send
      }
    }
    const options = {}
    strategy.authenticate(request, options)
    td.verify(send(td.matchers.contains(/saml/)))
  })

})
