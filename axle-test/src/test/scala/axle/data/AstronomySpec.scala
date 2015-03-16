package axle.data

import org.specs2.mutable._
import spire.implicits._
import spire.compat.ordering
import axle.quanta.Mass
import axle.quanta.Distance

class AstronomySpec extends Specification {

  "ordering celestial bodies by mass" should {
    "work" in {

      implicit val md = Mass.converterGraph[Double, axle.jung.JungDirectedGraph]
      implicit val dd = Distance.converterGraph[Double, axle.jung.JungDirectedGraph]
      val astro = axle.data.Astronomy()
      val sorted = astro.bodies.sortBy(_.mass)

      // sorted.foreach { b => println(b.name + " " + string(b.mass in md.kilogram)) }

      sorted.last.name must be equalTo "Andromeda Galaxy"
    }
  }

}