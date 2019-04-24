package axle.quantumcircuit

//import spire.math._
//import QBit._

import org.scalatest._

import spire.math.Real

import QBit._

/**
 * See https://youtu.be/F_Riqjdh2oM?t=2001
 */

class DeutschOracleSpec extends FunSuite with Matchers {

  implicit val fieldReal = new spire.math.RealAlgebra

  test("Deutsch Oracle of identity") {
    isConstantDeutschOracle(identityForDeutsch[Real] _) should be(false)
  }

  test("Deutsch Oracle of negate") {
    isConstantDeutschOracle(negateForDeutsch[Real] _) should be(false)
  }

  test("Deutsch Oracle of constant0") {
    isConstantDeutschOracle(constant0ForDeutsch[Real] _) should be(true)
  }

  test("Deutsch Oracle of constant1") {
    isConstantDeutschOracle(constant1ForDeutsch[Real] _) should be(true)
  }

}