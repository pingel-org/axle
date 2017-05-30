package axle.data

import org.scalatest._
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder
import spire.implicits._
import spire.implicits.DoubleAlgebra
import axle.quanta.Time
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class EvolutionSpec extends FunSuite with Matchers {

  test("Evolution data: development modern humans is most recent event") {

    implicit val tcg = Time.converterGraphK2[Double, DirectedSparseGraph]

    val evo = Evolution()

    import evo.history

    val recent = history.maxBy(_.timestamp)

    recent.e should be("Modern Humans")
  }

}
