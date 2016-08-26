package axle

import org.specs2.mutable.Specification

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.algebra.modules.doubleRationalModule
import spire.implicits.DoubleAlgebra
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph

class TrigonometrySpec extends Specification {

  implicit val amd = Angle.converterGraphK2[Double, DirectedSparseGraph]

  "sine(UnittedQuantity[Angle, Double])" should {
    "return Double" in {
      axle.sine(UnittedQuantity(2d, amd.radian)) must be equalTo math.sin(2d)
    }
  }

  "arcSine(Double)" should {
    "return UnittedQuantity[Angle, Double]" in {
      axle.arcSine(0.5) must be equalTo UnittedQuantity(math.asin(0.5d), amd.radian)
    }
  }

  "cosine(UnittedQuantity[Angle, Double])" should {
    "work" in {
      axle.cosine(UnittedQuantity(2d, amd.radian)) must be equalTo math.cos(2d)
    }
  }

  "arcCosine(Double)" should {
    "return UnittedQuantity[Angle, Double]" in {
      axle.arcCosine(0.5) must be equalTo UnittedQuantity(math.acos(0.5d), amd.radian)
    }
  }

  "tangent(UnittedQuantity[Angle, Double])" should {
    "work" in {
      axle.tangent(UnittedQuantity(2d, amd.radian)) must be equalTo math.tan(2d)
    }
  }

  "arcTangent(Double)" should {
    "return UnittedQuantity[Angle, Double]" in {
      axle.arcTangent(0.5) must be equalTo UnittedQuantity(math.atan(0.5d), amd.radian)
    }
  }

}