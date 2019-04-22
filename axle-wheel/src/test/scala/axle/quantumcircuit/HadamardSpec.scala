package axle.quantumcircuit

import org.scalatest._

class HadamardSpec extends FunSuite with Matchers {

  test("Hadamard Gate on |0> and |1>") {

    // Takes a 1 or 0 and puts them into exactly equal superposition

    // TODO
    // H|0> = (1/sqrt(2)  1/sqrt(2))
    // H|1> = (1/sqrt(2) -1/sqrt(2))
  }

  // TODO
  // can be implemented with a 2x2 matrix:
  // [1/sqrt(2) 1/sqrt(2); 1/sqrt(2) -1/sqrt(2)]

  test("Hadamard is its own inverse") {

    // TODO
    // H(1/sqrt(2)  1/sqrt(2)) = (1 0) = |0>
    // H(1/sqrt(2) -1/sqrt(2)) = (0 1) = |1>
  }

}