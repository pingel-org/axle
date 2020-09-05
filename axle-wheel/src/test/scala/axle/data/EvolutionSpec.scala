package axle.data

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._
import spire.algebra._

import axle.quanta.Time
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class EvolutionSpec extends AnyFunSuite with Matchers {

  test("Evolution data: development modern humans is most recent event") {

    implicit val tcg = {
      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      Time.converterGraphK2[Double, DirectedSparseGraph]
    }

    val evo = Evolution()

    implicit val mmd: MultiplicativeMonoid[Double] = spire.implicits.DoubleAlgebra
    //    implicit val ouq: Order[axle.quanta.UnittedQuantity[axle.quanta.Time, Double]] =
    //      axle.quanta.UnittedQuantity.orderUQ[axle.quanta.Time, Double]

    val recent = evo.history.maxBy(_.timestamp)

    recent.e should be("Modern Humans")
  }

}
