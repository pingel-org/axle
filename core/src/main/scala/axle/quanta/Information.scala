package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Information extends Quantum {

  type UOM = InformationUnit
  
  class InformationUnit(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): InformationUnit = new InformationUnit(conversion, name, symbol, link)

  def zero() = new InformationUnit(None, Some("zero"), Some("0"), None) with ZeroWithUnit
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Information"

  val derivations = Nil

  // link(mile, "1.609344", kilometer)

  val bit = unit("bit", "b")
  val nibble = quantity("4", bit, Some("nibble"))
  val byte = quantity("8", bit, Some("byte"), Some("B"), Some("http://en.wikipedia.org/wiki/Byte"))

  val kilobyte = quantity("1024", byte, Some("kilobyte"), Some("KB"))
  val KB = kilobyte
  
  val megabyte = quantity("1024", kilobyte, Some("megabyte"), Some("MB"))
  val MB = megabyte
  
  val gigabyte = quantity("1024", megabyte, Some("gigabyte"), Some("GB"))
  val GB = gigabyte
  
  val terabyte = quantity("1024", gigabyte, Some("terabyte"), Some("TB"))
  val TB = terabyte
  
  val petabyte = quantity("1024", terabyte, Some("petabyte"), Some("PB"))
  val PB = petabyte

}

object Information extends Information()
