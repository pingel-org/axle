package axle.data

import org.specs2.mutable._
import spire.implicits._
import spire.math._
import spire.compat.ordering
import spire.algebra.Module
import spire.math.Rational
import axle.quanta.Mass
import axle.quanta.Distance
import axle.quanta.Time
import axle.jung.JungDirectedGraph
import spire.algebra.Rng
import spire.algebra.AdditiveGroup
import spire.implicits.DoubleAlgebra
import spire.algebra.MultiplicativeSemigroup
import axle.algebra.modules.doubleRationalModule

class AstronomySpec extends Specification {

  "ordering celestial bodies by mass" should {
    "work" in {

      implicit val md = Mass.converterGraph[Double, JungDirectedGraph]
      implicit val dd = Distance.converterGraph[Double, JungDirectedGraph]
      implicit val td = Time.converterGraph[Double, JungDirectedGraph]
      val astro = axle.data.Astronomy()
      val sorted = astro.bodies.sortBy(_.mass)

      // sorted.foreach { b => println(b.name + " " + string(b.mass in md.kilogram)) }

      sorted.last.name must be equalTo "Andromeda Galaxy"
    }
  }

}