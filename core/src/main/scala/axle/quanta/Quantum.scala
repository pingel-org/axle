package axle.quanta

import axle.graph.NativeDirectedGraphFactory._

import java.math.BigDecimal
import math.{ max, abs }

/**
 * Quantum
 *
 * Used in the sense of the World English Dictionary's 4th definition:
 *
 * 4. something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

/**
 * TODO
 *
 * HSet for numerator + HSet for denominator ?
 * name, symbol, link for new units resulting from by and over
 * derive should heck that the given compound unit is in this quantum's list of derivations?
 * derive should add the compoundUnit to the graph?
 * reconcile newEdge(source, dest) and newEdge(source, dest, magnitude)
 */

trait Quantum {

  outer =>

  type UOM <: UnitOfMeasurement

  val conversionGraph = graph[UOM, BigDecimal]()

  type CGE = conversionGraph.type#E
  type CGV = conversionGraph.type#V

  implicit def toBD(i: Int) = new BigDecimal(i.toString)

  implicit def toBD(d: Double) = new BigDecimal(d.toString)

  implicit def toBD(s: String) = new BigDecimal(s)

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) =
    numerator.divide(
      denominator,
      max(max(numerator.precision, abs(numerator.scale)),
        max(denominator.precision, abs(denominator.scale))),
      java.math.RoundingMode.HALF_UP)

  val oneBD = new BigDecimal("1")

  trait UnitOfMeasurement {

    self: UOM =>

    def getConversion(): Option[CGE]
    def setConversion(cge: CGE): Unit
    def getLabel(): String
    def getSymbol(): Option[String]
    def getLink(): Option[String]

    def +(right: UOM): UOM
    def -(right: UOM): UOM
    def *(bd: BigDecimal): UOM
    def /(bd: BigDecimal): UOM

    def *:(bd: BigDecimal): UOM

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM
    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def in(other: UOM): UOM
  }

  class ZeroWithUnit() extends UnitOfMeasurement {

    self: UOM =>

    def getConversion(): Option[CGE] = None
    def setConversion(cge: CGE): Unit = {}
    def getLabel(): String = "zero with unit"
    def getSymbol(): Option[String] = Some("zero with unit")
    def getLink(): Option[String] = None

    def +(right: UOM): UOM = right
    def -(right: UOM): UOM = right * -1
    def *(bd: BigDecimal): UOM = self
    def /(bd: BigDecimal): UOM = self

    override def *:(bd: BigDecimal) = self
    // def in_:(bd: BigDecimal) = quantity(bd, this) // How would this be defined on the zero?

    // TODO: remove nulls
    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    def in(other: UOM): UOM = null.asInstanceOf[UOM]

  }

  class UnitOfMeasurementImpl(
    var conversion: Option[CGE],
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None) extends UnitOfMeasurement {

    self: UOM =>

    val quantum: Quantum = outer

    val vertex = conversionGraph += this
    uom2vertex += this -> vertex

    def getConversion() = conversion
    def setConversion(cge: CGE) = conversion = Some(cge)
    def getLabel() = name.getOrElse("")
    def getSymbol() = symbol
    def getLink() = link

    def kilo() = quantity(oneBD.scaleByPowerOfTen(3), this, Some("kilo" + name.getOrElse("")), Some("K" + symbol.getOrElse("")))
    def mega() = quantity(oneBD.scaleByPowerOfTen(6), this, Some("mega" + name.getOrElse("")), Some("M" + symbol.getOrElse("")))
    def giga() = quantity(oneBD.scaleByPowerOfTen(9), this, Some("giga" + name.getOrElse("")), Some("G" + symbol.getOrElse("")))
    def tera() = quantity(oneBD.scaleByPowerOfTen(12), this, Some("kilo" + name.getOrElse("")), Some("T" + symbol.getOrElse("")))
    def peta() = quantity(oneBD.scaleByPowerOfTen(15), this, Some("peta" + name.getOrElse("")), Some("P" + symbol.getOrElse("")))
    def exa() = quantity(oneBD.scaleByPowerOfTen(18), this, Some("exa" + name.getOrElse("")), Some("E" + symbol.getOrElse("")))
    def zetta() = quantity(oneBD.scaleByPowerOfTen(21), this, Some("zetta" + name.getOrElse("")), Some("Z" + symbol.getOrElse("")))
    def yotta() = quantity(oneBD.scaleByPowerOfTen(24), this, Some("yotta" + name.getOrElse("")), Some("Y" + symbol.getOrElse("")))

    def deci() = quantity(oneBD.scaleByPowerOfTen(-1), this, Some("deci" + name.getOrElse("")), Some("d" + symbol.getOrElse("")))
    def centi() = quantity(oneBD.scaleByPowerOfTen(-2), this, Some("centi" + name.getOrElse("")), Some("c" + symbol.getOrElse("")))
    def milli() = quantity(oneBD.scaleByPowerOfTen(-3), this, Some("milli" + name.getOrElse("")), Some("m" + symbol.getOrElse("")))
    def micro() = quantity(oneBD.scaleByPowerOfTen(-6), this, Some("micro" + name.getOrElse("")), Some("μ" + symbol.getOrElse("")))
    def nano() = quantity(oneBD.scaleByPowerOfTen(-9), this, Some("nano" + name.getOrElse("")), Some("n" + symbol.getOrElse("")))

    override def toString() = name.getOrElse(conversion
      .map(c => c.getPayload + " " + c.getSource.getPayload.getSymbol.getOrElse(""))
      .getOrElse(name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + this.getClass().getSimpleName()))

    def +(right: UOM): UOM = {
      val (bd, uom) = conversion.map(c => (c.getPayload, c.getSource.getPayload)).getOrElse((oneBD, this))
      quantity(bd.add((right in uom).getConversion.get.getPayload), uom) // TODO remove .get
    }

    def -(right: UOM): UOM = {
      val (bd, uom) = conversion.map(c => (c.getPayload, c.getSource.getPayload)).getOrElse((oneBD, this))
      quantity(bd.subtract((right in uom).getConversion.get.getPayload), uom) // TODO remove .get
    }

    def *(bd: BigDecimal): UOM = conversion
      .map(c => quantity(c.getPayload.multiply(bd), c.getSource.getPayload))
      .getOrElse(quantity(bd, this))

    def /(bd: BigDecimal): UOM = conversion
      .map(c => quantity(bdDivide(c.getPayload, bd), c.getSource.getPayload))
      .getOrElse(quantity(bdDivide(oneBD, bd), this))

    override def *:(bd: BigDecimal) = quantity(bd, this)

    def in_:(bd: BigDecimal) = quantity(bd, this)

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = conversion.map(c =>
        right.getConversion.map(rc => c.getPayload.multiply(rc.getPayload)
        ).getOrElse(c.getPayload)
      ).getOrElse(right.getConversion.map(_.getPayload)
        .getOrElse(oneBD)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = conversion.map(c =>
        bottom.getConversion.map(bc => bdDivide(c.getPayload, bc.getPayload)
        ).getOrElse(c.getPayload)
      ).getOrElse(bottom.getConversion.map(bc => bdDivide(oneBD, bc.getPayload))
        .getOrElse(oneBD)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def in(other: UOM): UOM = {
      val otherVertex = vertexFor(other)
      val thisVertex = vertexFor(this)
      val resultBD = conversionGraph.shortestPath(otherVertex, thisVertex).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: CGE) => bd.multiply(edge.getPayload))
      })
      if (resultBD.isEmpty) {
        throw new Exception("no conversion path from " + this + " to " + other)
      }

      quantity(resultBD.get, other)
    }

  }

  // def zero(): UOM

  def newUnitOfMeasurement(
    conversion: Option[CGE] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(None, Some(name), Some(symbol), linkOpt)

  def derive(compoundUnit: UnitOfMeasurement,
    nameOpt: Option[String] = None,
    symbolOpt: Option[String] = None,
    linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(None, nameOpt, symbolOpt, linkOpt)

  def link(base: UOM, multiple: BigDecimal, result: UOM): Unit = {
    val baseVertex = vertexFor(base)
    val resultVertex = vertexFor(result)
    conversionGraph += (resultVertex -> baseVertex, multiple)
    conversionGraph += (baseVertex -> resultVertex, bdDivide(oneBD, multiple))
  }

  var uom2vertex = Map[UOM, CGV]()

  def vertexFor(uom: UOM): CGV = uom2vertex(uom)

  def quantity(
    magnitude: BigDecimal,
    unit: UOM,
    qname: Option[String] = None,
    qsymbol: Option[String] = None,
    qlink: Option[String] = None): UOM = {

    val uom = newUnitOfMeasurement(None, qname, qsymbol, qlink)
    val uomVertex = vertexFor(uom)
    val unitVertex = vertexFor(unit)
    val conversion1 = conversionGraph += (uomVertex -> unitVertex, bdDivide(oneBD, magnitude))
    val conversion2 = conversionGraph += (unitVertex -> uomVertex, magnitude)
    uom.setConversion(conversion2)
    uom
  }

  val wikipediaUrl: String

  //  val derivations: List[Quantum]
  //
  //  def by(right: Quantum, resultQuantum: Quantum): Quantum = QuantumMultiplication(this, right, resultQuantum)
  //
  //  def over(bottom: Quantum, resultQuantum: Quantum): Quantum = QuantumMultiplication(this, bottom, resultQuantum)

  override def toString() = this.getClass().getSimpleName()

  /**
   * Searches the Directed Graph defined by this Quantum for a path of Conversions from source to goal
   * @param source Start node for shortest path search
   * @param goal End node for shortest path search
   */
  //  def conversionPath(source: UOM, goal: UOM): Option[List[Conversion]] =
  //    shortestPath(source.asInstanceOf[V], goal.asInstanceOf[V])

}

/**
case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum {

  type UOM = QRESULT#UOM

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[QRESULT#UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}

case class QuantumDivision[QTOP <: Quantum, QBOTTOM <: Quantum, QRESULT <: Quantum](top: QTOP, bottom: QBOTTOM, resultQuantum: QRESULT) extends Quantum {

  type UOM = QRESULT#UOM

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}
*/

/**
  object ConversionGraphFactoryObject extends ConversionGraphFactory
  trait ConversionGraphFactory extends JungDirectedGraphFactory {
    
    type G = ConversionGraph
    
    trait ConversionGraph extends JungDirectedGraph[UOM, BigDecimal] {

      type V = ConversionGraphVertex
      trait ConversionGraphVertex extends JungDirectedGraphVertex[UOM]

      type E = ConversionGraphEdge
      trait ConversionGraphEdge extends JungDirectedGraphEdge[BigDecimal]
    }
  }
  import ConversionGraphFactoryObject._
 * 
 * 
 */
