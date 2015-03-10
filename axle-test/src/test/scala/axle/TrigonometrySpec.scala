package axle

import org.specs2.mutable.Specification

import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung
import axle.quanta.Angle
import axle.quanta.conversionGraph
import axle.quanta.UnittedQuantity
import spire.implicits.DoubleAlgebra

class TrigonometrySpec extends Specification {

  "sine(angle)" should {

    "work" in {

      implicit val amd = Angle.metadata[Double]

      axle.sine(UnittedQuantity(2d, amd.radian)) must be equalTo math.sin(2d)

    }
  }

  "cosine(angle)" should {

    "work" in {

      implicit val amd = Angle.metadata[Double]

      axle.cosine(UnittedQuantity(2d, amd.radian)) must be equalTo math.cos(2d)

    }
  }

  "tangent(angle)" should {

    "work" in {

      implicit val amd = Angle.metadata[Double]

      axle.tangent(UnittedQuantity(2d, amd.radian)) must be equalTo math.tan(2d)

    }
  }

}