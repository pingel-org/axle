package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Energy extends Quantum {

  type Q = EnergyQuantity
  type UOM = EnergyUnit

  class EnergyUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): EnergyUnit = new EnergyUnit(name, symbol, link)

  class EnergyQuantity(magnitude: BigDecimal, unit: EnergyUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: EnergyUnit): EnergyQuantity = new EnergyQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  import Power.{ kilowatt }
  import Time.{ hour }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Energy"

  lazy val _conversionGraph = JungDirectedGraph[EnergyUnit, BigDecimal](
    List(
      derive(kilowatt.by[Time.type, this.type](hour, this)),
      unit("joule", "J"),
      unit("kilojoule", "KJ"),
      unit("megajoule", "MJ"),
      unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent")),
      unit("kiloton", "KT"),
      unit("megaton", "MT"),
      unit("gigaton", "GT")
    ),
    (vs: Seq[JungDirectedGraphVertex[EnergyUnit]]) => vs match {
      case kwh :: j :: kj :: mj :: t :: kt :: mt :: gt :: Nil => List(
        (mj, t, "4.184"),
        (j, kj, "1E3"),
        (j, mj, "1E6"),
        (t, kt, "1E3"),
        (t, mt, "1E6"),
        (t, gt, "1E9")
      )
    }
  )

  lazy val kwh = byName("kwh")
  lazy val joule = byName("joule")
  lazy val kilojoule = byName("kilojoule")
  lazy val megajoule = byName("megajoule")
  lazy val tonTNT = byName("ton TNT")
  lazy val kiloton = byName("kiloton")
  lazy val megaton = byName("megaton")
  lazy val gigaton = byName("gigaton")

}

object Energy extends Energy()
