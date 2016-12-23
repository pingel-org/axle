package axle.data

import org.scalatest._
import spire.implicits._
import spire.compat.ordering
import axle.quanta.Time
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import cats.implicits._

class EvolutionSpec extends FunSuite with Matchers {

  test("Evolution data: development modern humans is most recent event") {

    implicit val tcg = Time.converterGraphK2[Double, DirectedSparseGraph]

    val evo = Evolution()

    import evo.history

    val recent = history.maxBy(_.timestamp)

    recent.e should be("Modern Humans")
  }

}
