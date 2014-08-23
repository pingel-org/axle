package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Speed[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends Quantum[N](space) {
 
  class SpeedQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = SpeedQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): SpeedQuantity =
    new SpeedQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: SpeedQuantity): SpeedQuantity =
    new SpeedQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"

}

object Speed extends Speed[Rational](rationalDoubleMetricSpace) {

  import Distance.{ meter, mile, ft, km }
  import Time.{ second, hour }

  lazy val _conversionGraph = conversions(
    List(
      derive(meter.over[Time.type, this.type](second, this), Some("mps")),
      derive(ft.over[Time.type, this.type](second, this), Some("fps")),
      derive(mile.over[Time.type, this.type](hour, this), Some("mph")),
      derive(km.over[Time.type, this.type](hour, this), Some("kph")),
      unit("Knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)")),
      unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light")),
      unit("Speed limit", "speed limit")),
    (vs: Seq[Vertex[SpeedQuantity]]) => vs match {
      case mps :: fps :: mph :: kph :: kn :: c :: speedLimit :: Nil => trips2fns(List(
        (kn, kph, Number(1.852)),
        (c, mps, 299792458),
        (mph, speedLimit, 65)))
      case _ => Nil
    })

  lazy val mps = byName("mps")
  lazy val fps = byName("fps")
  lazy val mph = byName("mph")
  lazy val kph = byName("kph")
  lazy val knot = byName("Knot")
  lazy val kn = knot
  lazy val c = byName("Light Speed")
  lazy val speedLimit = byName("Speed limit")

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph

}
