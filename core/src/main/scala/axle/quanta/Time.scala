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
    ),
    (vs: Seq[JungDirectedGraphVertex[TimeUnit]]) => vs match {
      case Nil => List(
      )
    }
  )

  lazy val second = unit("second", "s")
  lazy val millisecond = second milli
  lazy val microsecond = second micro
  lazy val nanosecond = second nano
  lazy val s = second
  lazy val ms = millisecond
  lazy val μs = microsecond
  lazy val ns = nanosecond
  lazy val hour = unit("hour", "hr")
  lazy val day = unit("day", "d")
  lazy val year = quantity("365.25", day, Some("year"), Some("yr"), Some("http://en.wikipedia.org/wiki/Year"))
  lazy val century = quantity("100", year, Some("century"), None, Some("http://en.wikipedia.org/wiki/Century"))
  lazy val millenium = quantity("1000", year, Some("millenium"), None, Some("http://en.wikipedia.org/wiki/Millenium"))  
  lazy val ky = millenium
  lazy val my = quantity("1000000", year, Some("million year"), Some("my"), None)
  lazy val gy = quantity("1000000000", year, Some("billion year"), Some("gy"), None)

  lazy val globalLifeExpectancy = quantity("67.2", year, Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  // Distant Past:
  lazy val universeAge = quantity("13.7", gy, Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  lazy val earthAge = quantity("4.54", gy, Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  lazy val simpleCellsAge = quantity("3.8", gy, Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val multiCellularLifeAge = quantity("1", gy, Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val fungiAge = quantity("560", my, Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val classMammalAge = quantity("215", my, Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val primateAge = quantity("60", my, Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val australopithecusAge = quantity("4", my, Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  lazy val modernHumanAge = quantity("200", ky, Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  
}

object Time extends Time()
