package axle.data

import org.specs2.mutable._
import spire.implicits._
import spire.compat.ordering
import axle.quanta.Time
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung

class EvolutionSpec extends Specification {

  "Evolution data" should {
    "development modern humans is most recent event" in {

      implicit val tcg = Time.converterGraphK2[Double, DirectedSparseGraph]

      val evo = Evolution()

      import evo.history

      val recent = history.maxBy(_.timestamp)

      recent.e must be equalTo "Modern Humans"
    }
  }

}