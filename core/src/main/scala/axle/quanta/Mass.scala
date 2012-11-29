package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Mass extends Quantum {

  class MassQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MassQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassQuantity =
    new MassQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: MassQuantity): MassQuantity =
    new MassQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  lazy val _conversionGraph = JungDirectedGraph[MassQuantity, BigDecimal](
    List(
      unit("gram", "g"),
      unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne")),
      unit("milligram", "mg"),
      unit("kilogram", "Kg"),
      unit("megagram", "Mg"),
      unit("kilotonne", "KT"),
      unit("megatonne", "MT"),
      unit("gigatonne", "GT"),
      unit("teratonne", "TT"),
      unit("petatonne", "PT"),
      unit("exatonne", "ET"),
      unit("zettatonne", "ZT"),
      unit("yottatonne", "YT")
    ),
    (vs: Seq[JungDirectedGraphVertex[MassQuantity]]) => vs match {
      case g :: t :: mg :: kg :: meg :: kt :: mt :: gt :: tt :: pt :: et :: zt :: yt :: Nil =>
        withInverses(List(
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
        ))
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
