package axle

/**
 *
 *
 * http://en.wikipedia.org/wiki/Bcrypt
 *
 */

class Bcrypt {

  import math.{ pow }
  import axle._

  case class BcryptState(var P: Array[Array[Byte]], var S: Array[Array[Array[Byte]]])

  def initState() = {

    // TODO
    // populating the P-array and S-box entries with
    // the fractional part of pi in hexadecimal

    val P = List(List(0.toByte).toArray).toArray // TODO
    val S = List(List(List(0.toByte).toArray).toArray).toArray // TODO

    BcryptState(P, S)
  }

  def encryptECB(state: BcryptState, ctext: Array[Byte]): Array[Byte] = {
    // TODO
    null
  }

  def encrypt(x: Array[Byte]): Array[Byte] = {
    // TODO
    null
  }

  def expandKey(state: BcryptState, salt: Array[Byte], key: Array[Byte]): BcryptState = {

    import state._

    for (n <- 1 to 18) {
      P(n) = key(32 * (n - 1) to 32 * n - 1) ⊕ P(n) //treat the key as cyclic
    }
    var ctext = encrypt(salt(0 to 63))
    P(1) = ctext(0 to 31)
    P(2) = ctext(32 to 63)
    for (n <- 2 to 9) {
      ctext = encrypt(ctext ⊕ salt(64 * (n - 1) to 64 * n - 1)) //encrypt using the current key schedule and treat the salt as cyclic
      P(2 * n - 1) = ctext(0 to 31)
      P(2 * n) = ctext(32 to 63)
    }
    for (i <- 1 to 4) {
      for (n <- 0 to 127) {
        ctext = encrypt(ctext ⊕ salt(64 * (n - 1) to 64 * n - 1)) //as above
        S(i)(2 * n) = ctext(0 to 31)
        S(i)(2 * n + 1) = ctext(32 to 63)
      }
    }
    state
  }

  def eksBlowfishSetup(cost: Int, salt: Array[Byte], key: Array[Byte]): BcryptState = {
    var state = initState()
    state = expandKey(state, salt, key)
    for (i <- 0 until pow(2, cost).toInt) {
      state = expandKey(state, null, key) // vs List(0x0.toByte).toArray ?
      state = expandKey(state, null, salt)
    }
    state
  }

  def bcrypt(cost: Int, salt: Array[Byte], input: Array[Byte]) = {
    var state = eksBlowfishSetup(cost, salt, input)
    var ctext = "OrpheanBeholderScryDoubt".getBytes //three 64-bit blocks
    for (i <- 0 until 64) {
      ctext = encryptECB(state, ctext) //encrypt using standard Blowfish in ECB mode
    }
    (List(cost.toByte) ++ salt.toList ++ ctext.toList).toArray // SLOW
  }

}