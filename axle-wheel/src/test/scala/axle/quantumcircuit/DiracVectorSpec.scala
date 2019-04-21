package axle.quantumcircuit

import axle.algebra.Binary

import org.scalatest._

class DiracVectorSpec extends FunSuite with Matchers {

  test("CBit to Dirac Vector Notation") {

    CBit0.reverseIndex should be(|("0").>.reverseIndex)

    CBit1.reverseIndex should be(|("1").>.reverseIndex)
  }

  test("Multiple bits are represented as tensored product vector") {

    |("00").>.reverseIndex should be(Vector[Binary](1, 0) ⊗ Vector[Binary](1, 0))
    |("00").>.reverseIndex should be(CBit0.reverseIndex ⊗ CBit0.reverseIndex)
    |("00").>.reverseIndex should be(Vector[Binary](1, 0, 0, 0))

    |("01").>.reverseIndex should be(Vector[Binary](1, 0) ⊗ Vector[Binary](0, 1))
    |("01").>.reverseIndex should be(CBit0.reverseIndex ⊗ CBit1.reverseIndex)
    |("01").>.reverseIndex should be(Vector[Binary](0, 1, 0, 0))

    |("10").>.reverseIndex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](1, 0))
    |("10").>.reverseIndex should be(CBit1.reverseIndex ⊗ CBit0.reverseIndex)
    |("10").>.reverseIndex should be(Vector[Binary](0, 0, 1, 0))

    |("11").>.reverseIndex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](0, 1))
    |("11").>.reverseIndex should be(CBit1.reverseIndex ⊗ CBit1.reverseIndex)
    |("11").>.reverseIndex should be(Vector[Binary](0, 0, 0, 1))
  }

  test("Dirac Vector of non-base-2 numbers") {

    import DiracVector.intStringToVectorBinary

    |("4", intStringToVectorBinary).>.reverseIndex should be(|("100").>.reverseIndex)
    |("4", intStringToVectorBinary).>.reverseIndex should be(Vector[Binary](0, 1) ⊗ Vector[Binary](1, 0) ⊗ Vector[Binary](1, 0))
    |("4", intStringToVectorBinary).>.reverseIndex should be(Vector[Binary](0, 0, 0, 0, 1, 0, 0, 0))
  }

}