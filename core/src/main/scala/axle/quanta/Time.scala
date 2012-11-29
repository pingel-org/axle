package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

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

  lazy val _conversionGraph = JungDirectedGraph[TimeQuantity, BigDecimal](
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
    (vs: Seq[JungDirectedGraphVertex[TimeQuantity]]) => vs match {
      case s :: ms :: μs :: ns :: m :: hr :: d :: y :: c :: ky :: my :: gy :: Nil => List(
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
      )
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

}

object Time extends Time()
