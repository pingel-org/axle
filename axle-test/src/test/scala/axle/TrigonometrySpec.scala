package axle

import org.scalatest._

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.algebra.modules.doubleRationalModule
import spire.implicits.DoubleAlgebra
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph

class TrigonometrySpec extends FunSuite with Matchers {

  implicit val amd = Angle.converterGraphK2[Double, DirectedSparseGraph]

  test("sine(UnittedQuantity[Angle, Double]) => Double") {
    axle.sine(UnittedQuantity(2d, amd.radian)) should be(math.sin(2d))
  }

  test("arcSine(Double) => UnittedQuantity[Angle, Double]") {
    axle.arcSine(0.5) should be(UnittedQuantity(math.asin(0.5d), amd.radian))
  }

  test("cosine(UnittedQuantity[Angle, Double]) => Double") {
    axle.cosine(UnittedQuantity(2d, amd.radian)) should be(math.cos(2d))
  }

  test("arcCosine(Double) => UnittedQuantity[Angle, Double]") {
    axle.arcCosine(0.5) should be(UnittedQuantity(math.acos(0.5d), amd.radian))
  }

  test("tangent(UnittedQuantity[Angle, Double]) => Double") {
    axle.tangent(UnittedQuantity(2d, amd.radian)) should be(math.tan(2d))
  }

  test("arcTangent(Double) => UnittedQuantity[Angle, Double]") {
    axle.arcTangent(0.5) should be(UnittedQuantity(math.atan(0.5d), amd.radian))
  }

}
