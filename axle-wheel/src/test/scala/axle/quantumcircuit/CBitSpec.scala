package axle.quantumcircuit

import org.scalatest._

import axle.quantumcircuit.CBit._

class CBitSpec extends FunSuite with Matchers {

  // TODO put Binary.negate and others in typeclass
  // TODO put CBit.negate and others in typeclass

  test("functions of 1 CBit: identity") {
    identity(CBit0) should be(CBit0)
    identity(CBit1) should be(CBit1)
  }

  test("functions of 1 CBit: negate") {
    negate(CBit0) should be(CBit1)
    negate(CBit1) should be(CBit0)
  }

  test("functions of 1 CBit: constant0") {
    constant0(CBit0) should be(CBit0)
    constant0(CBit1) should be(CBit0)
  }

  test("functions of 1 CBit: constant1") {
    constant1(CBit0) should be(CBit1)
    constant1(CBit1) should be(CBit1)
  }

  test("CNOT") {

    cnot(CBit0, CBit0) should be((CBit0, CBit0))
    cnot(CBit0, CBit1) should be((CBit0, CBit1))
    cnot(CBit1, CBit0) should be((CBit1, CBit1))
    cnot(CBit1, CBit1) should be((CBit1, CBit0))
  }

  // TODO Vector versions of above
  // def identity[T](cbit: Vector[T]): Vector[T] = cbit

  // TODO matrix versions of above
  // identity  as matrix: [1 0; 0 1]
  // negate    as matrix: [0 1; 1 0]
  // constant0 as matrix: [1 1; 0 0]
  // constant1 as matrix: [0 0; 1 1]

  // TODO CNOT as matrix as a matrix for a 2-CBit tensored product
  //  [1 0 0 0; 0 1 0 0; 0 0 0 1; 0 0 1 0]

  // TODO CNOT + "Toffoli" gate (aka "CCNOT") is a "complete" / "universal" set

  // TODO:
  // Identity and Negate are reversible
  // Quantum circuits only composed of reversible functions that are their own inverses

}
