package axle.data

import org.specs2.mutable._
import spire.implicits._
import spire.compat.ordering
import axle.quanta.Mass
import axle.quanta.Distance
import axle.quanta.Time
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung

class AstronomySpec extends Specification {

  "ordering celestial bodies by mass" should {
    "work" in {

      implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
      implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
      implicit val td = Time.converterGraphK2[Double, DirectedSparseGraph]
      val astro = axle.data.Astronomy()
      val sorted = astro.bodies.sortBy(_.mass)

      // sorted.foreach { b => println(b.name + " " + string(b.mass in md.kilogram)) }

      sorted.last.name must be equalTo "Andromeda Galaxy"
    }
  }

}