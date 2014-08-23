package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Frequency[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends Quantum[N](space) {
  
  class FrequencyQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = FrequencyQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FrequencyQuantity =
    new FrequencyQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: FrequencyQuantity): FrequencyQuantity =
    new FrequencyQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Frequency"

}

object Frequency extends Frequency[Rational](rationalDoubleMetricSpace) {

  lazy val _conversionGraph = conversions(
    List(
      unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz")),
      unit("Kilohertz", "KHz"),
      unit("Megahertz", "MHz"),
      unit("Gigahertz", "GHz")),
    (vs: Seq[Vertex[FrequencyQuantity]]) => vs match {
      case hz :: khz :: mhz :: ghz :: Nil => trips2fns(List(
        (hz, khz, 1E3),
        (hz, mhz, 1E9),
        (hz, ghz, 1E12)))
      case _ => Nil
    })

  lazy val hertz = byName("Hertz")
  lazy val kilohertz = byName("Kilohertz")
  lazy val megahertz = byName("Megahertz")
  lazy val gigahertz = byName("Gigahertz")

  lazy val Hz = hertz
  lazy val KHz = kilohertz
  lazy val MHz = megahertz
  lazy val GHz = gigahertz

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph

}
