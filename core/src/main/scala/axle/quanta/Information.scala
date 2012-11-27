package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Information extends Quantum {

  type Q = InformationQuantity
  type UOM = InformationUnit

  case class InformationUnit(
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None)
    extends UnitOfMeasurementImpl(_name, _symbol, _link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): InformationUnit = new InformationUnit(name, symbol, link)

  class InformationQuantity(magnitude: BigDecimal, unit: InformationUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: InformationUnit): InformationQuantity = new InformationQuantity(magnitude, unit)

  def zero() = new InformationUnit(Some("zero"), Some("0"), None) with ZeroWithUnit

  def conversionGraph() = _conversionGraph

  lazy val _conversionGraph = JungDirectedGraph[InformationUnit, BigDecimal](
    List(
      unit("bit", "b"),
      unit("nibble", "nibble"),
      unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte")),
      unit("kilobyte", "KB"),
      unit("megabyte", "MB"),
      unit("gigabyte", "GB"),
      unit("terabyte", "TB"),
      unit("petabyte", "PB")
    ),
    (vs: Seq[JungDirectedGraphVertex[InformationUnit]]) => vs match {
      case bit :: nibble :: byte :: kilobyte :: megabyte :: gigabyte :: terabyte :: petabyte :: Nil => List(
        (bit, nibble, "4"),
        (bit, byte, "8"),
        (byte, kilobyte, "1024"),
        (kilobyte, megabyte, "1024"),
        (megabyte, gigabyte, "1024"),
        (gigabyte, terabyte, "1024"),
        (terabyte, petabyte, "1024")
      )
    }
  )

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Information"

  val derivations = Nil

  val bit = byName("bit")
  val nibble = byName("nibble")
  val byte = byName("byte")
  val kilobyte = byName("kilobyte")
  val megabyte = byName("megabyte")
  val gigabyte = byName("gigabyte")
  val terabyte = byName("terabyte")
  val petabyte = byName("petabyte")

  val KB = kilobyte
  val MB = megabyte
  val GB = gigabyte
  val TB = terabyte
  val PB = petabyte

}

object Information extends Information()
