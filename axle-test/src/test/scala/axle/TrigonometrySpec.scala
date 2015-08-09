package axle

import org.specs2.mutable.Specification

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import axle.algebra.modules.doubleRationalModule
import spire.implicits.DoubleAlgebra
import spire.algebra.Module
import spire.math.Rational
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph

class TrigonometrySpec extends Specification {
 
  "sine(angle)" should {

    "work" in {

      implicit val amd = Angle.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double]]

      axle.sine(UnittedQuantity(2d, amd.radian)) must be equalTo math.sin(2d)

    }
  }

  "cosine(angle)" should {

    "work" in {

      implicit val amd = Angle.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double]]

      axle.cosine(UnittedQuantity(2d, amd.radian)) must be equalTo math.cos(2d)

    }
  }

  "tangent(angle)" should {

    "work" in {

      implicit val amd = Angle.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double]]

      axle.tangent(UnittedQuantity(2d, amd.radian)) must be equalTo math.tan(2d)

    }
  }

}