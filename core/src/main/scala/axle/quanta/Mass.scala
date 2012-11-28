package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Mass extends Quantum {

  type Q = MassQuantity
  type UOM = MassUnit

  class MassUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassUnit = new MassUnit(name, symbol, link)

  class MassQuantity(magnitude: BigDecimal, unit: MassUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: MassUnit): MassQuantity = new MassQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  lazy val _conversionGraph = JungDirectedGraph[MassUnit, BigDecimal](
    List(
      unit("gram", "g"),
      unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne")),
      unit("milligram", "mg"),
      unit("kilogram", "Kg"),
      unit("megagram", "Mg"),
      unit("kilotonne", "KT"),
      unit("megatonne", "MT"),
      unit("gigatonne", "GT"),
      unit("tera", "TT"),
      unit("peta", "PT"),
      unit("exa", "ET"),
      unit("zetta", "ZT"),
      unit("yotta", "YT")
    ),
    (vs: Seq[JungDirectedGraphVertex[MassUnit]]) => vs match {
      case g :: t :: mg :: kg :: meg :: kt :: mt :: gt :: tt :: pt :: et :: zt :: yt :: Nil => List(
        (t, meg, 1),
        (mg, g, "1E3"),
        (g, kg, "1E3"),
        (g, meg, "1E6"),
        (t, kt, "1E3"),
        (t, mt, "1E6"),
        (t, gt, "1E9"),
        (t, tt, "1E12"),
        (t, pt, "1E15"),
        (t, et, "1E18"),
        (t, zt, "1E21"),
        (t, tt, "1E24")
      )
    }
  )

  lazy val gram = byName("gram")
  lazy val tonne = byName("tonne")
  lazy val milligram = byName("milligram")
  lazy val kilogram = byName("kilogram")
  lazy val megagram = byName("megagram")
  lazy val kilotonne = byName("kilotonne")
  lazy val megatonne = byName("megatonne")
  lazy val gigatonne = byName("gigatonne")
  lazy val teratonne = byName("teratonne")
  lazy val petatonne = byName("petatonne")
  lazy val exatonne = byName("exatonne")
  lazy val zettatonne = byName("zettatonne")
  lazy val yottatonne = byName("yottatonne")
}

object Mass extends Mass()
