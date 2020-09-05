package axle.quantumcircuit

import cats.implicits._
import spire.math._

import axle.syntax.probabilitymodel._
import axle.algebra.RegionEq

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class QBitSpec extends AnyFunSuite with Matchers {

  test("fair coin QBit") {

    val sqrtHalf = Complex(Real(1) / sqrt(Real(2)), Real(0))

    val qEven = QBit[Real](sqrtHalf, sqrtHalf)

    val distribution = qEven.probabilityModel

    distribution.P(RegionEq(CBit0)) should be(Real(1 / 2d))
    distribution.P(RegionEq(CBit1)) should be(Real(1 / 2d))
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
    distribution.P(RegionEq(|("00").>().unindex)) should be(Real(1 / 4d))
    distribution.P(RegionEq(|("01").>().unindex)) should be(Real(1 / 4d))
    distribution.P(RegionEq(|("10").>().unindex)) should be(Real(1 / 4d))
    distribution.P(RegionEq(|("11").>().unindex)) should be(Real(1 / 4d))
  }

  import axle.quantumcircuit.QBit._
  import spire.algebra.Field
  implicit val fieldReal: Field[Real] = new spire.math.RealAlgebra()
  val QBit0 = constant0[Real]
  val QBit1 = constant1[Real]

  test("functions of 1 QBit: identity") {
    identity(QBit0) should be(QBit0)
    identity(QBit1) should be(QBit1)
  }

  test("functions of 1 QBit: negate") {
    negate(QBit0) should be(QBit1)
    negate(QBit1) should be(QBit0)
  }

  test("functions of 1 QBit: constant0") {
    constant0(QBit0) should be(QBit0)
    constant0(QBit1) should be(QBit0)
  }

  test("functions of 1 QBit: constant1") {
    constant1(QBit0) should be(QBit1)
    constant1(QBit1) should be(QBit1)
  }

  test("CNOT") {

    QBit2.cnot(QBit2(QBit0.unindex ⊗ QBit0.unindex)).unindex should be((QBit0.unindex ⊗ QBit0.unindex))
    QBit2.cnot(QBit2(QBit0.unindex ⊗ QBit1.unindex)).unindex should be((QBit0.unindex ⊗ QBit1.unindex))
    QBit2.cnot(QBit2(QBit1.unindex ⊗ QBit0.unindex)).unindex should be((QBit1.unindex ⊗ QBit1.unindex))
    QBit2.cnot(QBit2(QBit1.unindex ⊗ QBit1.unindex)).unindex should be((QBit1.unindex ⊗ QBit0.unindex))
  }

}