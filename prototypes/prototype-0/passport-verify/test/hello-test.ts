import * as assert from 'assert'
import sayHello from '../lib/passport-verify'

describe('Say hello', function () {

  it('should say hello world', function () {
    assert.equal('hello world!', sayHello())
  })

})
