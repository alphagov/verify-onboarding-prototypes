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
  verifyServiceProviderHost: string,
  logger: any
}

export class PassportVerifyStrategy extends Strategy {

  public name: string = 'verify'

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

const nullLogger = {
  log: () => undefined,
  info: () => undefined,
  error: () => undefined,
  debug: () => undefined,
  warn: () => undefined
}

export function createStrategy (options: PassportVerifyOptions) {
  const logger = options.logger || nullLogger

  const loggedFetch = (method: string, url: string, headers?: any, requestBody?: string) => {
    logger.log('passport-verify', method, url, requestBody || '')
    return fetch(url, {
      method: method,
      headers: headers,
      body: requestBody
    }).then(response => {
      return response.text().then(responseBody => {
        logger.log('passport-verify', `${response.status} ${response.statusText}`, responseBody)
        return responseBody
      })
    })
  }

  const getAuthnRequestPromise = () => {
    return loggedFetch('POST', options.verifyServiceProviderHost + '/generate-request')
        .then(body => JSON.parse(body) as AuthnRequestResponse)
  }

  const translateResponsePromise = (samlResponse: string, secureToken: string) => {
    return loggedFetch('POST', options.verifyServiceProviderHost + '/translate-response',
        { 'Content-Type': 'application/json' },
        `{ "response": "${samlResponse}", "secureToken": "${secureToken}" }`
    ).then(body => JSON.parse(body) as TranslatedResponseBody)
  }

  return new PassportVerifyStrategy(getAuthnRequestPromise, translateResponsePromise)
}
