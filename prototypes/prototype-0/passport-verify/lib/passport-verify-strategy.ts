import { Strategy } from 'passport-strategy'
import * as express from 'express'
import fetch from 'node-fetch'

// Usage options:
//   new PassportVerifyStrategy({options...}) // TODO: how to inject http
//   passportVerifyStrategyFactory({options...}) // http injected by factory
//   

export function createStrategy(options?: any) {
  return new PassportVerifyStrategy(() => {
    return fetch('/generate-authn-request')
      .then(x => x.json<object>())
      .then(x => x)
  })
}

export default class PassportVerifyStrategy extends Strategy {

  constructor(private getAuthnRequestPromise: () => Promise<object>) {
    super()
  }

  authenticate (req: express.Request, options?: any) {
    this.getAuthnRequestPromise()
      .then(() => (req as any).res.send('saml'))
  }

}
