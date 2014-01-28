package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

class Time extends Quantum {

  class TimeQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = TimeQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): TimeQuantity =
    new TimeQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: TimeQuantity): TimeQuantity =
    new TimeQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph: DirectedGraph[Q, Number => Number] = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
  // "http://en.wikipedia.org/wiki/Time"

  lazy val _conversionGraph = conversions(
    List(
      unit("second", "s", Some("http://en.wikipedia.org/wiki/Second")),
      unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond")),
      unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond")),
      unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond")),
      unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond")),
      unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond")),
      unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond")),
      unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond")),
      unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond")),
      unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute")),
      unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour")),
      unit("day", "d", Some("http://en.wikipedia.org/wiki/Day")),
      unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year")),
      unit("century", "century", Some("http://en.wikipedia.org/wiki/Century")),
      unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium")),
      unit("megayear", "my"),
      unit("gigayear", "gy")),
    (vs: Seq[Vertex[TimeQuantity]]) => vs match {
      case s :: ms :: μs :: ns :: ps :: fs :: as :: zs :: ys :: m :: hr :: d :: y :: c :: ky :: my :: gy :: Nil => trips2fns(List(
        (ms, s, 1E3),
        (μs, s, 1E6),
        (ns, s, 1E9),
        (ps, s, 1E12),
        (fs, s, 1E15),
        (as, s, 1E18),
        (zs, s, 1E21),
        (ys, s, 1E24),
        (s, m, 60),
        (m, hr, 60),
        (hr, d, 24),
        (d, y, 365.25),
        (y, c, 1E2),
        (y, ky, 1E3),
        (y, my, 1E6),
        (y, gy, 1E9)))
      case _ => Nil
    })

  lazy val second = byName("second")
  lazy val millisecond = byName("millisecond")
  lazy val microsecond = byName("microsecond")
  lazy val nanosecond = byName("nanosecond")
  lazy val minute = byName("minute")
  lazy val hour = byName("hour")
  lazy val day = byName("day")
  lazy val year = byName("year")
  lazy val century = byName("century")
  lazy val millenium = byName("millenium")
  lazy val my = byName("megayear")
  lazy val gy = byName("gigayear")

  lazy val ky = millenium
  lazy val s = second
  lazy val m = minute
  lazy val ms = millisecond
  lazy val μs = microsecond
  lazy val ns = nanosecond

  lazy val globalLifeExpectancy = 67.2 *: year // Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  // Distant Past:
  lazy val universeAge = 13.7 *: gy // Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  lazy val earthAge = 4.54 *: gy // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  lazy val simpleCellsAge = 3.8 *: gy // Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val multiCellularLifeAge = 1 *: gy // Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val fungiAge = 560 *: my // Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val classMammalAge = 215 *: my // Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val primateAge = 60 *: my // Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val australopithecusAge = 4 *: my // Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val modernHumanAge = 200 *: ky // Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))

}

object Time extends Time()
