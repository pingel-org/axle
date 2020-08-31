package axle.data

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra._

import axle.quanta.Distance
import axle.quanta.Flow
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class RiversSpec extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("rivers data") {

    implicit val dcg = Distance.converterGraphK2[Double, DirectedSparseGraph]
    implicit val fcg = Flow.converterGraphK2[Double, DirectedSparseGraph]

    val rivers = Rivers()

    import rivers._

    val longest: River = List(nile, mississippi).maxBy(_.length)

    longest.name should be("Nile")
  }

}
