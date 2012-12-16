package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Speed extends Quantum {

  class SpeedQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = SpeedQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): SpeedQuantity =
    new SpeedQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: SpeedQuantity): SpeedQuantity =
    new SpeedQuantity(magnitude, Some(unit), None, None, None)

  import Distance.{ meter, mile, ft }
  import Time.{ second, hour }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"

  def conversionGraph() = _conversionGraph

  lazy val _conversionGraph = conversions(
    List(
      derive(meter.over[Time.type, this.type](second, this)),
      derive(ft.over[Time.type, this.type](second, this)),
      derive(mile.over[Time.type, this.type](hour, this)),
      unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light")),
      unit("Speed limit", "speed limit")
    ),
    (vs: Seq[Vertex[SpeedQuantity]]) => vs match {
      case mps :: fps :: mph :: c :: speedLimit :: Nil => withInverses(List(
        (c, mps, "299792458"),
        (mph, speedLimit, "65")
      ))
      case _ => Nil
    }
  )

  lazy val mps = byName("mps")
  lazy val fps = byName("fps")
  lazy val mph = byName("mph")
  lazy val c = byName("c")
  lazy val speedLimit = byName("Speed limit")

}

object Speed extends Speed()
