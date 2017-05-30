package axle.data

import org.scalatest._
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.Order.catsKernelOrderingForOrder
import spire.implicits.DoubleAlgebra
import axle.quanta._
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class AstronomySpec extends FunSuite with Matchers {

  test("ordering celestial bodies by mass") {

    implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
    implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
    implicit val td = Time.converterGraphK2[Double, DirectedSparseGraph]

    val astro = axle.data.Astronomy()
    val sorted = astro.bodies.sortBy(_.mass)

    sorted.last.name should be("Andromeda Galaxy")
  }

}
