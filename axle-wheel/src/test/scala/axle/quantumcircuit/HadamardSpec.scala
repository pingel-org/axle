package axle.quantumcircuit

import spire.algebra._
import spire.math._

import QBit._

import org.scalatest._

class HadamardSpec extends FunSuite with Matchers {

  implicit val fieldReal: Field[Real] = new spire.math.RealAlgebra

  val sqrtHalf = Complex(Real(1) / sqrt(Real(2)), Real(0))

  test("Hadamard Gate on |0> and |1>") {

    // Takes a 1 or 0 and puts them into exactly equal superposition

    // H|0> = (1/sqrt(2)  1/sqrt(2))
    hadamard(constant0[Real]) should be(QBit[Real](sqrtHalf, sqrtHalf))

    // H|1> = (1/sqrt(2) -1/sqrt(2))
    hadamard(constant1[Real]) should be(QBit[Real](sqrtHalf, -sqrtHalf))
  }

  test("Hadamard is its own inverse") {

    // H(1/sqrt(2)  1/sqrt(2)) = (1 0) = |0>
    hadamard(hadamard(constant0[Real])) should be(constant0[Real])
  
    // H(1/sqrt(2) -1/sqrt(2)) = (0 1) = |1>
    hadamard(hadamard(constant1[Real])) should be(constant1[Real])
  }

}