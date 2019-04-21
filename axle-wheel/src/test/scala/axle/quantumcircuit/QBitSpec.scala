package axle.quantumcircuit

import spire.math._

import axle.algebra._
import axle.syntax.probabilitymodel._

import org.scalatest._

class QBitSpec extends FunSuite with Matchers {

  test("fair coin QBit") {

    val sqrtHalf = Complex(Real(1) / sqrt(Real(2)), Real(0))

    val qEven = QBit[Real](sqrtHalf, sqrtHalf)

    val distribution = qEven.probabilityModel

    distribution.P(B0) should be(Real(1 / 2d))
    distribution.P(B1) should be(Real(1 / 2d))
  }

  test("multiple QBit distribution") {

    val half = Complex(Real(1) / Real(2), Real(0))

    val sqrtHalf = Complex(Real(1) / sqrt(Real(2)), Real(0))

    val qEven = QBit[Real](sqrtHalf, sqrtHalf)

    // As with cbits we represent multiple qbits with their tensor product
    val tensored = qEven.unindex ⊗ qEven.unindex

    val distribution = unindexToDistribution(tensored)

    tensored.zip(tensored).map({ case (x, y) => x * y}).reduce(_+_).real should be(Real(1))
    tensored should be(Vector[Complex[Real]](half, half, half, half))
    distribution.P(|("00").>.unindex) should be(Real(1 / 4d))
    distribution.P(|("01").>.unindex) should be(Real(1 / 4d))
    distribution.P(|("10").>.unindex) should be(Real(1 / 4d))
    distribution.P(|("11").>.unindex) should be(Real(1 / 4d))
  }

  test("1-bit functions") {

    // All the 1-bit functions (id, negate, constant-0, and constant-1) work the same
    // TODO
  }

  test("CNOT") {

    // CNOT still works, too
    // TODO
  }

}