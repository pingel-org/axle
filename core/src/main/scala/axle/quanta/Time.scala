package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Time extends Quantum {

  class TimeQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = TimeQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): TimeQuantity =
    new TimeQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: TimeQuantity): TimeQuantity =
    new TimeQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
  // "http://en.wikipedia.org/wiki/Time"

  lazy val _conversionGraph = conversions(
    List(
      unit("second", "s"),
      unit("millisecond", "ms"),
      unit("microsecond", "μs"),
      unit("nanosecond", "ns"),
      unit("minute", "m"),
      unit("hour", "hr"),
      unit("day", "d"),
      unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year")),
      unit("century", "century", Some("http://en.wikipedia.org/wiki/Century")),
      unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium")),
      unit("megayear", "my"),
      unit("gigayear", "gy")
    ),
    (vs: Seq[Vertex[TimeQuantity]]) => vs match {
      case s :: ms :: μs :: ns :: m :: hr :: d :: y :: c :: ky :: my :: gy :: Nil => withInverses(List(
        (ms, s, "1E3"),
        (μs, s, "1E6"),
        (ns, s, "1E9"),
        (s, m, 60),
        (m, hr, 60),
        (hr, d, 24),
        (d, y, "365.25"),
        (y, c, "1E2"),
        (y, ky, "1E3"),
        (y, my, "1E6"),
        (y, gy, "1E9")
      ))
    }
  )

  lazy val second = byName("second")
  lazy val millisecond = byName("millisecond")
  lazy val microsecond = byName("microsecond")
  lazy val nanosecond = byName("nanosecond")
  lazy val hour = byName("hour")
  lazy val day = byName("day")
  lazy val year = byName("year")
  lazy val century = byName("century")
  lazy val millenium = byName("millenium")
  lazy val my = byName("megayear")
  lazy val gy = byName("gigayear")

  lazy val ky = millenium
  lazy val s = second
  lazy val ms = millisecond
  lazy val μs = microsecond
  lazy val ns = nanosecond

  lazy val globalLifeExpectancy = "67.2" *: year // Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  // Distant Past:
  lazy val universeAge = "13.7" *: gy // Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  lazy val earthAge = "4.54" *: gy // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  lazy val simpleCellsAge = "3.8" *: gy // Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val multiCellularLifeAge = "1" *: gy // Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val fungiAge = "560" *: my // Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val classMammalAge = "215" *: my // Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val primateAge = "60" *: my // Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val australopithecusAge = "4" *: my // Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val modernHumanAge = "200" *: ky // Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))

}

object Time extends Time()
