package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Volume extends Quantum {

  class VolumeQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = VolumeQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): VolumeQuantity =
    new VolumeQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: VolumeQuantity): VolumeQuantity =
    new VolumeQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  import Distance.{ meter, km }
  import Area.{ m2, km2 }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"

  lazy val _conversionGraph = conversions(
    List(
      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
      unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
    ),
    (vs: Seq[V[VolumeQuantity]]) => vs match {
      case m3 :: km3 :: greatLakes :: Nil => withInverses(List(
        (km3, greatLakes, 22671)
      ))
    }
  )

  lazy val m3 = byName("m3")
  lazy val km3 = byName("km3")
  lazy val greatLakes = byName("Great Lakes Volume")

}

object Volume extends Volume()
