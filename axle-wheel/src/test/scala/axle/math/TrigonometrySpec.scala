package axle

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra._

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class TrigonometrySpec extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra

  implicit val amd = Angle.converterGraphK2[Double, DirectedSparseGraph]

  test("sine(UnittedQuantity[Angle, Double]) => Double") {
    axle.math.sine(UnittedQuantity(2d, amd.radian)) should be(scala.math.sin(2d))
  }

  test("arcSine(Double) => UnittedQuantity[Angle, Double]") {
    axle.math.arcSine(0.5) should be(UnittedQuantity(scala.math.asin(0.5d), amd.radian))
  }

  test("cosine(UnittedQuantity[Angle, Double]) => Double") {
    axle.math.cosine(UnittedQuantity(2d, amd.radian)) should be(scala.math.cos(2d))
  }

  test("arcCosine(Double) => UnittedQuantity[Angle, Double]") {
    axle.math.arcCosine(0.5) should be(UnittedQuantity(scala.math.acos(0.5d), amd.radian))
  }

  test("tangent(UnittedQuantity[Angle, Double]) => Double") {
    axle.math.tangent(UnittedQuantity(2d, amd.radian)) should be(scala.math.tan(2d))
  }

  test("arcTangent(Double) => UnittedQuantity[Angle, Double]") {
    axle.math.arcTangent(0.5) should be(UnittedQuantity(scala.math.atan(0.5d), amd.radian))
  }

}
