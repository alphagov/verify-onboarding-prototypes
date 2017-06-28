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
  name: string,
  value: string
}

export interface TranslatedResponseBody {
  pid: string,
  levelOfAssurance: string,
  attributes: Attribute[]
}

export interface PassportVerifyOptions {
  verifyServiceProviderHost: string,
  logger: any,
  acceptUser: (user: TranslatedResponseBody) => any
}

export const USER_NOT_ACCEPTED_ERROR = Symbol('The user was not accepted by the application.')

export class PassportVerifyStrategy extends Strategy {

  public name: string = 'verify'

  constructor (private generateRequestPromise: () => Promise<AuthnRequestResponse>,
               private translateResponsePromise: (samlResponse: string, secureToken: string) => Promise<TranslatedResponseBody>,
               private acceptUser: (user: TranslatedResponseBody) => any) {
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
      .then(translatedResponseBody => Promise.all([this.acceptUser(translatedResponseBody), translatedResponseBody]))
      .then(values => {
        const [user, translatedResponseBody] = values
        if (user) {
          this.success(user, translatedResponseBody)
        } else {
          this.fail(USER_NOT_ACCEPTED_ERROR)
        }
      })
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
  const logger = options.logger || {
    info: () => undefined
  }

  const loggedFetch = (method: string, url: string, headers?: any, requestBody?: string) => {
    logger.info('passport-verify', method, url, requestBody || '')
    return fetch(url, {
      method: method,
      headers: headers,
      body: requestBody
    }).then(response => {
      return response.text().then(responseBody => {
        logger.info('passport-verify', `${response.status} ${response.statusText}`, responseBody)
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

  return new PassportVerifyStrategy(getAuthnRequestPromise, translateResponsePromise, options.acceptUser)
}
