package axle.quantumcircuit

import org.scalatest._

import spire.algebra._
import spire.math._
import spire.random.Dist

import QBit._

/**
 * See https://youtu.be/F_Riqjdh2oM?t=2001
 */

class DeutschOracleSpec extends FunSuite with Matchers {

  implicit val fieldReal: Field[Real] = new spire.math.RealAlgebra

  implicit val distReal: Dist[Real] =
    new spire.random.DistFromGen[Real](g => Real(g.nextDouble(0, 1)))

  test("constant0ForDeutsch works") {

    val f = wrapDeutsched(constant0ForDeutsch[Real])

    f(constant0) should be(constant0)
    f(constant1) should be(constant0)
  }

  test("constant1ForDeutsch works") {

    val f = wrapDeutsched(constant1ForDeutsch[Real])

    f(constant0) should be(constant1)
    f(constant1) should be(constant1)
  }

  test("identityForDeutsch works") {

    val f = wrapDeutsched(identityForDeutsch[Real])

    f(constant0) should be(constant0)
    f(constant1) should be(constant1)
  }

  test("negateForDeutsch works") {

    val f = wrapDeutsched(negateForDeutsch[Real])

    f(constant0) should be(constant1)
    f(constant1) should be(constant0)
  }

  test("Deutsch Oracle of constant0") {

    isConstantDeutschOracle(constant0ForDeutsch[Real]) should be(true)
  }

  test("Deutsch Oracle of constant1") {
    isConstantDeutschOracle(constant1ForDeutsch[Real]) should be(true)
  }

  test("Deutsch Oracle of identity") {
    isConstantDeutschOracle(identityForDeutsch[Real]) should be(false)
  }

  test("Deutsch Oracle of negate") {
    isConstantDeutschOracle(negateForDeutsch[Real]) should be(false)
  }

}