package org.pingel.axle.quanta

import java.math.BigDecimal

class Time extends Quantum {

  type UOM = TimeUnit

  class TimeUnit(
    conversion: Option[Conversion] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[Conversion] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): TimeUnit = new TimeUnit(conversion, name, symbol, link)
  

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
  // "http://en.wikipedia.org/wiki/Time"
  val derivations = Nil

  val second = unit("second", "s")
  val millisecond = second milli
  val microsecond = second micro
  val nanosecond = second nano
  val s = second
  val ms = millisecond
  val μs = microsecond
  val ns = nanosecond
  val hour = unit("hour", "hr")
  val day = unit("day", "d")
  val year = quantity("365.25", day, Some("year"), Some("yr"), Some("http://en.wikipedia.org/wiki/Year"))
  val century = quantity("100", year, Some("century"), None, Some("http://en.wikipedia.org/wiki/Century"))
  val millenium = quantity("1000", year, Some("millenium"), None, Some("http://en.wikipedia.org/wiki/Millenium"))  
  val ky = millenium
  val my = quantity("1000000", year, Some("million year"), Some("my"), None)
  val gy = quantity("1000000000", year, Some("billion year"), Some("gy"), None)

  val globalLifeExpectancy = quantity("67.2", year, Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  // Distant Past:
  val universeAge = quantity("13.7", gy, Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  val earthAge = quantity("4.54", gy, Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  val simpleCellsAge = quantity("3.8", gy, Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val multiCellularLifeAge = quantity("1", gy, Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val fungiAge = quantity("560", my, Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val classMammalAge = quantity("215", my, Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val primateAge = quantity("60", my, Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val australopithecusAge = quantity("4", my, Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val modernHumanAge = quantity("200", ky, Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  
}

object Time extends Time()
