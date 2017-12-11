package axle.data

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._
import spire.algebra._

import axle.quanta.Time
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class EvolutionSpec extends FunSuite with Matchers {

  test("Evolution data: development modern humans is most recent event") {

    implicit val tcg = {
      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      Time.converterGraphK2[Double, DirectedSparseGraph]
    }

    val evo = Evolution()

    // history: List[Event[Double, V, String]]
    val recent = evo.history.maxBy(_.timestamp)

    recent.e should be("Modern Humans")
  }

}
