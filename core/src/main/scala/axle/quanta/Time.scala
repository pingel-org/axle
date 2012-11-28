package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Time extends Quantum {

  type Q = TimeQuantity
  type UOM = TimeUnit

  class TimeUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): TimeUnit = new TimeUnit(name, symbol, link)

  class TimeQuantity(magnitude: BigDecimal, unit: TimeUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: TimeUnit): TimeQuantity = new TimeQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
  // "http://en.wikipedia.org/wiki/Time"

  lazy val _conversionGraph = JungDirectedGraph[TimeUnit, BigDecimal](
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
    (vs: Seq[JungDirectedGraphVertex[TimeUnit]]) => vs match {
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
