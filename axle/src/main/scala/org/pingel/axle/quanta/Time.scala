package org.pingel.axle.quanta

object Time extends Quantum {

  import Quantity._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
  // "http://en.wikipedia.org/wiki/Time"
  val derivations = Nil

  val second = UnitOfMeasurement(this, "second", "s")
  val millisecond = second milli
  val microsecond = second micro
  val nanosecond = second nano
  val s = second
  val ms = millisecond
  val μs = microsecond
  val ns = nanosecond
  val hour = UnitOfMeasurement(this, "hour", "hr")
  val day = UnitOfMeasurement(this, "day", "d")
  val year = Quantity("365.25", day, Some("year"), Some("yr"), Some("http://en.wikipedia.org/wiki/Year"))
  val century = Quantity("100", year, Some("century"), None, Some("http://en.wikipedia.org/wiki/Century"))
  val millenium = Quantity("1000", year, Some("millenium"), None, Some("http://en.wikipedia.org/wiki/Millenium"))  
  val ky = millenium
  val my = Quantity("1000000", year, Some("million year"), Some("my"), None)
  val gy = Quantity("1000000000", year, Some("billion year"), Some("gy"), None)

  val globalLifeExpectancy = Quantity("67.2", year, Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  // Distant Past:
  val universeAge = Quantity("13.7", gy, Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  val earthAge = Quantity("4.54", gy, Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  val simpleCellsAge = Quantity("3.8", gy, Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val multiCellularLifeAge = Quantity("1", gy, Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val fungiAge = Quantity("560", my, Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val classMammalAge = Quantity("215", my, Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val primateAge = Quantity("60", my, Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val australopithecusAge = Quantity("4", my, Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  val modernHumanAge = Quantity("200", ky, Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))

}