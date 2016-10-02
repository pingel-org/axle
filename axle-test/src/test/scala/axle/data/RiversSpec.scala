package axle.data

import org.specs2.mutable._
import spire.implicits._
import spire.compat.ordering
import axle.quanta.Distance
import axle.quanta.Flow
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung

class RiversSpec extends Specification {

  "rivers data" should {
    "Order Nile longer than Mississippi" in {

      implicit val dcg = Distance.converterGraphK2[Double, DirectedSparseGraph]
      implicit val fcg = Flow.converterGraphK2[Double, DirectedSparseGraph]

      val rivers = Rivers()

      import rivers._

      val longest: River = List(nile, mississippi).maxBy(_.length)

      longest.name must be equalTo "Nile"
    }
  }

}