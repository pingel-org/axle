package axle.data

import spire.algebra.Field
import spire.implicits.leftModuleOps
import axle.quanta._

case class CelestialBody(
  name:        String,
  symbol:      Option[String],
  mass:        UnittedQuantity[Mass, Double],
  wikpediaUrl: String)

case class Astronomy()(
  implicit
  mc: MassConverter[Double], dc: DistanceConverter[Double], tc: TimeConverter[Double]) {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

  import mc._
  import dc._
  import tc._

  lazy val earth = CelestialBody("earth", Some("⊕"), 5.9736 *: zettatonne, "http://en.wikipedia.org/wiki/Earth")

  lazy val sun = CelestialBody("sun", Some("☉"), 1.9891E30 *: kilogram, "http://en.wikipedia.org/wiki/Solar_mass")

  lazy val jupiter = CelestialBody("jupiter", Some("♃"), 1.8986 *: yottatonne, "http://en.wikipedia.org/wiki/Jupiter")

  lazy val saturn = CelestialBody("saturn", Some("♄"), 568.46 *: zettatonne, "http://en.wikipedia.org/wiki/Saturn")

  lazy val neptune = CelestialBody("neptune", Some("♆"), 102.43 *: zettatonne, "http://en.wikipedia.org/wiki/Neptune")

  lazy val uranus = CelestialBody("uranus", Some("♅"), 86.810 *: zettatonne, "http://en.wikipedia.org/wiki/Uranus")

  lazy val venus = CelestialBody("venus", Some("♀"), 4.868 *: zettatonne, "http://en.wikipedia.org/wiki/Venus")

  lazy val mars = CelestialBody("mars", Some("♂"), 641.85 *: exatonne, "http://en.wikipedia.org/wiki/Mars")

  lazy val mercury = CelestialBody("mercury", Some("☿"), 330.22 *: exatonne, "http://en.wikipedia.org/wiki/Mercury_(planet)")

  lazy val pluto = CelestialBody("pluto", Some("♇"), 13.05 *: exatonne, "http://en.wikipedia.org/wiki/Pluto")

  lazy val moon = CelestialBody("moon", Some("☽"), 73.477 *: exatonne, "http://en.wikipedia.org/wiki/Moon")

  /**
   * http://en.wikipedia.org/wiki/Astronomical_symbols
   */
  lazy val ⊕ = earth
  lazy val ☼ = sun
  lazy val ☉ = sun
  lazy val ♃ = jupiter
  lazy val ♄ = saturn
  lazy val ♆ = neptune
  lazy val ♅ = uranus
  lazy val ♀ = venus
  lazy val ♂ = mars
  lazy val ☿ = mercury
  lazy val ♇ = pluto
  lazy val ☽ = moon

  lazy val milkyWay = CelestialBody("Milky Way Galaxy", None, 5.8E11 *: sun.mass, "http://en.wikipedia.org/wiki/Milky_Way")

  lazy val milkyWayDiameter = 100000d *: lightyear

  lazy val andromeda = CelestialBody("Andromeda Galaxy", None, 7.1E11 *: sun.mass, "http://en.wikipedia.org/wiki/Andromeda_Galaxy")

  lazy val toAndromeda = 2.6E6 *: lightyear

  lazy val bodies = List(
    earth, sun, jupiter, saturn, neptune, uranus, venus, mars,
    mercury, pluto, moon, milkyWay, andromeda)

  /**
   * http://en.wikipedia.org/wiki/Age_of_the_Universe
   */

  lazy val universeAge = 13.7 *: gigayear

  lazy val earthAge = 4.54 *: gigayear // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))

}
