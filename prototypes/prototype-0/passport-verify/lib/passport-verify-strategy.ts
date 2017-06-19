import { Strategy } from 'passport-strategy'
import * as express from 'express'
import fetch from 'node-fetch'
import { createSamlForm } from './saml-form'

export interface AuthnRequestResponse {
  samlRequest: string,
  secureToken: string,
  location: string
}

export interface Attribute {
  key: string,
  value: string
}

export interface TranslatedResponseBody {
  pid: string,
  levelOfAssurance: string,
  attributes: Attribute[]
}

export interface PassportVerifyOptions {
  verifyServiceProviderHost: string
}

export class PassportVerifyStrategy extends Strategy {

  public name: string = 'passport-verify'

  constructor (private generateRequestPromise: () => Promise<AuthnRequestResponse>,
               private translateResponsePromise: (samlResponse: string, secureToken: string) => Promise<TranslatedResponseBody>) {
    super()
  }

  authenticate (req: express.Request, options?: any) {
    return this._handleRequest(req)
      .catch(reason => this.error(reason))
  }

  _handleRequest (req: express.Request) {
    if (req.body && req.body.SAMLResponse) {
      return this._translateResponse(req.body.SAMLResponse)
    } else {
      return this._renderAuthnRequest((req as any).res)
    }
  }

  _translateResponse (samlResponse: string) {
    return this.translateResponsePromise(samlResponse, 'TODO secure-cookie')
      .then(translatedResponseBody => this.success(translatedResponseBody, {}))
  }

  _renderAuthnRequest (response: express.Response): Promise<express.Response> {
    return this.generateRequestPromise()
      .then(authnRequestResponse => response.send(createSamlForm(authnRequestResponse.location, authnRequestResponse.samlRequest)))
  }

  success (user: any, info: any) { throw new Error('`success` should be overridden by passport') }
  fail (argv1: any, argv2?: any) { throw new Error('`fail` should be overridden by passport') }
  error (reason: Error) { throw reason }
}

export function createStrategy (options: PassportVerifyOptions) {
  const getAuthnRequestPromise = () => {
    return fetch(options.verifyServiceProviderHost + '/generate-request', {
      method: 'POST'
    }).then(x => x.json<AuthnRequestResponse>())
  }

  const translateResponsePromise = (samlResponse: string, secureToken: string) => {
    return fetch(options.verifyServiceProviderHost + '/translate-response', { method: 'POST' })
      .then(x => x.json<TranslatedResponseBody>())
  }

  return new PassportVerifyStrategy(getAuthnRequestPromise, translateResponsePromise)
}