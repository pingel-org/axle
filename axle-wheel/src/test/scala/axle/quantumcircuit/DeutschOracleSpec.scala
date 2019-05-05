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

    val f = wrapDeutsched[Real](constant0ForDeutsch)

    f(constant0) should be(constant0)
    f(constant1) should be(constant0)
  }

  test("Deutsch Oracle of constant0") {

    isConstantDeutschOracle[Real](constant0ForDeutsch) should be(true)
  }

  test("constant1ForDeutsch works") {

    val f = wrapDeutsched[Real](constant1ForDeutsch)

    f(constant0) should be(constant1)
    f(constant1) should be(constant1)
  }

  test("Deutsch Oracle of constant1") {
    isConstantDeutschOracle[Real](constant1ForDeutsch) should be(true)
  }

  test("identityForDeutsch works") {

    val f = wrapDeutsched[Real](identityForDeutsch)

    f(constant0) should be(identity(constant0))
    f(constant1) should be(identity(constant1))
  }

  test("Deutsch Oracle of identity") {
    isConstantDeutschOracle[Real](identityForDeutsch) should be(false)
  }

  test("negateForDeutsch works") {

    val f = wrapDeutsched[Real](negateForDeutsch)

    f(constant0) should be(negate(constant0))
    f(constant1) should be(negate(constant1))
  }

  test("Deutsch Oracle of negate") {
    isConstantDeutschOracle[Real](negateForDeutsch) should be(false)
  }

}