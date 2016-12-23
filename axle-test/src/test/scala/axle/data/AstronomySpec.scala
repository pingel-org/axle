package axle.data

import org.scalatest._
import spire.implicits.DoubleAlgebra
import axle.quanta._
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import axle.orderToOrdering
import cats.implicits._

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
