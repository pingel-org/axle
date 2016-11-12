package axle.data

import org.specs2.mutable._
import spire.implicits._
import axle.quanta.Mass
import axle.quanta.Distance
import axle.quanta.Time
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import axle.orderToOrdering
import cats.implicits._

class AstronomySpec extends Specification {

  "ordering celestial bodies by mass" should {
    "work" in {

      implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
      implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
      implicit val td = Time.converterGraphK2[Double, DirectedSparseGraph]
      val astro = axle.data.Astronomy()
      val sorted = astro.bodies.sortBy(_.mass)

      sorted.last.name must be equalTo "Andromeda Galaxy"
    }
  }

}
