import { Strategy } from 'passport-strategy'
import * as express from 'express'
import fetch from 'node-fetch'
import { Response } from 'node-fetch'
import { createSamlForm } from './saml-form'

export interface AuthnRequestResponse {
  samlRequest: string,
  secureToken: string,
  location: string
}

export interface Address {
  verified?: boolean,
  lines?: string[],
  postCode?: string,
  internationalPostCode?: string,
  uprn?: string,
  fromDate?: string,
  toDate?: string
}

export interface Attributes {
  firstName?: string,
  firstNameVerified?: boolean,
  middleName?: string,
  middleNameVerified?: boolean,
  surname?: string,
  surnameVerified?: boolean,
  dateOfBirth?: string,
  dateOfBirthVerified?: boolean,
  address?: Address,
  cycle3?: string
}

export interface TranslatedResponseBody {
  pid: string,
  levelOfAssurance: string,
  attributes?: Attributes
}

export interface ErrorBody {
  reason: string,
  message: string
}

export interface PassportVerifyOptions {
  verifyServiceProviderHost: string,
  logger: any,
  acceptUser: (user: TranslatedResponseBody) => any
}

export const USER_NOT_ACCEPTED_ERROR = Symbol('The user was not accepted by the application.')
export const AUTHENTICATION_FAILED_ERROR = Symbol('AUTHENTICATION_FAILED')

export class VerifyServiceProviderError extends Error {
  constructor (public reason: string, public message: string, public status: number) {
    super(message)
  }
}

export class PassportVerifyStrategy extends Strategy {

  public name: string = 'verify'

  constructor (private generateRequestPromise: () => Promise<AuthnRequestResponse>,
               private translateResponsePromise: (samlResponse: string, secureToken: string) => Promise<TranslatedResponseBody>,
               private acceptUser: (user: TranslatedResponseBody) => any) {
    super()
  }

  async authenticate (req: express.Request, options?: any) {
    try {
      await this._handleRequest(req)
    } catch (error) {
      if (error instanceof VerifyServiceProviderError) {
        this.fail(error.reason, error.status)
      } else {
        this.error(error)
      }
    }
  }

  _handleRequest (req: express.Request) {
    if (req.body && req.body.SAMLResponse) {
      return this._translateResponse(req.body.SAMLResponse)
    } else {
      return this._renderAuthnRequest((req as any).res)
    }
  }

  async _translateResponse (samlResponse: string) {
    const translatedResponseBody = await this.translateResponsePromise(samlResponse, 'TODO secure-cookie')
    const user = await this.acceptUser(translatedResponseBody)
    if (user) {
      this.success(user, translatedResponseBody)
    } else {
      this.fail(USER_NOT_ACCEPTED_ERROR)
    }
  }

  async _renderAuthnRequest (response: express.Response): Promise<express.Response> {
    const authnRequestResponse = await this.generateRequestPromise()
    return response.send(createSamlForm(authnRequestResponse.location, authnRequestResponse.samlRequest))
  }

  success (user: any, info: any) { throw new Error('`success` should be overridden by passport') }
  fail (argv1: any, argv2?: any) { throw new Error('`fail` should be overridden by passport') }
  error (reason: Error) { throw reason }
}

export function createStrategy (options: PassportVerifyOptions) {
  const logger = options.logger || {
    info: () => undefined
  }

  function parseBody (response: Response, body: string) {
    if (response.headers.get('content-type').includes('application/json')) return JSON.parse(body)
    else return body
  }

  function rejectErrors (status: number, body: any) {
    if (status > 299) throw new VerifyServiceProviderError(body.reason || body, body.message, status)
    return body
  }

  async function loggedFetch (method: string, url: string, headers?: any, requestBody?: string) {
    logger.info('passport-verify', method, url, requestBody || '')
    const response = await fetch(url, {
      method: method,
      headers: headers,
      body: requestBody
    })
    const body = await response.text()
    logger.info('passport-verify', `${response.status} ${response.statusText}`, body)
    const parsedBody = parseBody(response, body)
    rejectErrors(response.status, parsedBody)
    return parsedBody
  }

  async function getAuthnRequestPromise () {
    return loggedFetch('POST', options.verifyServiceProviderHost + '/generate-request')
  }

  async function translateResponsePromise (samlResponse: string, secureToken: string) {
    return await loggedFetch('POST', options.verifyServiceProviderHost + '/translate-response',
        { 'Content-Type': 'application/json' },
        `{ "response": "${samlResponse}", "secureToken": "${secureToken}" }`)
  }

  return new PassportVerifyStrategy(getAuthnRequestPromise, translateResponsePromise, options.acceptUser)
}
