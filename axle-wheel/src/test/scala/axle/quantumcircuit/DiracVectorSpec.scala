package axle.quantumcircuit

import axle.algebra.Binary

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class DiracVectorSpec extends AnyFunSuite with Matchers {

  test("CBit to Dirac Vector Notation") {

    CBit0.unindex should be(|("0").>().unindex)

    CBit1.unindex should be(|("1").>().unindex)
  }

  test("Multiple bits are represented as tensored product vector") {

    |("00").>().unindex should be(Vector[Binary](1, 0) ⊗ Vector[Binary](1, 0))
    |("00").>().unindex should be(CBit0.unindex ⊗ CBit0.unindex)
    |("00").>().unindex should be(Vector[Binary](1, 0, 0, 0))

    |("01").>().unindex should be(Vector[Binary](1, 0) ⊗ Vector[Binary](0, 1))
    |("01").>().unindex should be(CBit0.unindex ⊗ CBit1.unindex)
    |("01").>().unindex should be(Vector[Binary](0, 1, 0, 0))

    |("10").>().unindex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](1, 0))
    |("10").>().unindex should be(CBit1.unindex ⊗ CBit0.unindex)
    |("10").>().unindex should be(Vector[Binary](0, 0, 1, 0))

    |("11").>().unindex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](0, 1))
    |("11").>().unindex should be(CBit1.unindex ⊗ CBit1.unindex)
    |("11").>().unindex should be(Vector[Binary](0, 0, 0, 1))
  }

  test("Dirac Vector of non-base-2 numbers") {

    import DiracVector.intStringToVectorBinary

    |("4", intStringToVectorBinary).>().unindex should be(|("100").>().unindex)
    |("4", intStringToVectorBinary).>().unindex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](1, 0) ⊗ Vector[Binary](1, 0))
    |("4", intStringToVectorBinary).>().unindex should be(Vector[Binary](0, 0, 0, 0, 1, 0, 0, 0))
  }

}