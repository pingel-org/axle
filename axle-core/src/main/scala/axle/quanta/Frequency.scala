package axle.quanta

import spire.algebra._
import spire.math._
import axle.graph._

class Frequency extends Quantum {

  class FrequencyQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = FrequencyQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean = x equals y // TODO
  }
  
  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FrequencyQuantity =
    new FrequencyQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: FrequencyQuantity): FrequencyQuantity =
    new FrequencyQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Frequency"

  lazy val _conversionGraph = conversions(
    List(
      unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz")),
      unit("Kilohertz", "KHz"),
      unit("Megahertz", "MHz"),
      unit("Gigahertz", "GHz")
    ),
    (vs: Seq[Vertex[FrequencyQuantity]]) => vs match {
      case hz :: khz :: mhz :: ghz :: Nil => trips2fns(List(
        (hz, khz, 1E3),
        (hz, mhz, 1E9),
        (hz, ghz, 1E12)
      ))
      case _ => Nil
    }
  )

  lazy val hertz = byName("Hertz")
  lazy val kilohertz = byName("Kilohertz")
  lazy val megahertz = byName("Megahertz")
  lazy val gigahertz = byName("Gigahertz")

  lazy val Hz = hertz
  lazy val KHz = kilohertz
  lazy val MHz = megahertz
  lazy val GHz = gigahertz

}

object Frequency extends Frequency()
