package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Volume extends Quantum {

  type Q = VolumeQuantity
  type UOM = VolumeUnit

  class VolumeUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): VolumeUnit = new VolumeUnit(name, symbol, link)

  class VolumeQuantity(magnitude: BigDecimal, unit: VolumeUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: VolumeUnit): VolumeQuantity = new VolumeQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  import Distance.{ meter, km }
  import Area.{ m2, km2 }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"

  lazy val _conversionGraph = JungDirectedGraph[VolumeUnit, BigDecimal](
    List(
      derive(m2.by[Distance.type, this.type](meter, this), Some("cubic meters"), Some("m^3")),
      derive(km2.by[Distance.type, this.type](km, this), Some("cubic kilometers"), Some("km^3")),
      unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
    ),
    (vs: Seq[JungDirectedGraphVertex[VolumeUnit]]) => vs match {
      case m3 :: km3 :: greatLakes :: Nil => List(
        (km3, greatLakes, 22671)
      )
    }
  )

  lazy val m3 = byName("m3")
  lazy val km3 = byName("km3")
  lazy val greatLakes = byName("Great Lakes Volume")

}

object Volume extends Volume()
