package axle.quanta

import axle.graph.JungDirectedGraph._
import java.math.BigDecimal
import math.{ max, abs }
import collection._

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

  type Q <: Quantity
  type UOM <: UnitOfMeasurement

  def conversionGraph(): JungDirectedGraph[UOM, BigDecimal]

  def byName(unitName: String): UOM = conversionGraph.findVertex(_.payload.name == unitName).get.payload

  implicit def toBD(i: Int) = new BigDecimal(i.toString)

  implicit def toBD(d: Double) = new BigDecimal(d.toString)

  implicit def toBD(s: String) = new BigDecimal(s)

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) = numerator.divide(
    denominator,
    max(max(numerator.precision, abs(numerator.scale)),
      max(denominator.precision, abs(denominator.scale))),
    java.math.RoundingMode.HALF_UP)

  val oneBD = new BigDecimal("1")
  val zeroBD = new BigDecimal("0")

  trait Quantity {

    def magnitude(): BigDecimal
    def unit(): UOM

    def +(right: Q): Q =
      quantity(this.magnitude.add((this in right.unit).magnitude), right.unit)

    def -(right: Q): Q =
      quantity(this.magnitude.subtract((this in right.unit).magnitude), right.unit)

    def *(bd: BigDecimal): Q = bd.doubleValue match {
      case 0.0 => zero()
      case _ => quantity(this.magnitude.multiply(bd), this.unit)
    }

    def /(bd: BigDecimal): Q = quantity(bdDivide(this.magnitude, bd), this.unit)

    // def *:(bd: BigDecimal): UOM

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(this.magnitude.multiply(right.magnitude), resultQuantum.newUnitOfMeasurement(None, None, None))

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(bdDivide(this.magnitude, bottom.magnitude), resultQuantum.newUnitOfMeasurement(None, None, None))

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def in(other: UOM): UOM
  }

  trait UnitOfMeasurement extends Quantity {

    self: UOM =>

    def name(): String
    def label(): String
    def symbol(): Option[String]
    def link(): Option[String]
  }

  trait ZeroWithUnit extends Quantity {

    self: Q =>

    override def +(right: Q): Q = right
    override def -(right: Q): Q = right * -1.0
    override def *(bd: BigDecimal): Q = self
    override def /(bd: BigDecimal): Q = self

    // override by, over, through, per, and in?
  }

  class QuantityImpl(magnitude: BigDecimal, unit: UOM) extends Quantity {

    def magnitude() = magnitude
    def unit() = unit

    def in(other: UOM): Q =
      conversionGraph.shortestPath(vertexFor(other), vertexFor(this.unit)).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: DirectedGraphEdge[UOM, BigDecimal]) => bd.multiply(edge.payload))
      })
        .map(bd => quantity(this.magnitude.multiply(bd), other))
        .getOrElse(throw new Exception("no conversion path from " + this + " to " + other))

  }

  class UnitOfMeasurementImpl(
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends UnitOfMeasurement {

    self: UOM =>

    val quantum: Quantum = outer

    def name() = _name.getOrElse("")
    def label() = _name.getOrElse("")
    def symbol() = _symbol
    def link() = _link

    def magnitude() = oneBD
    def unit() = this

    def vertex() = quantum.conversionGraph.findVertex(_.payload == this).get

    //    def kilo() = quantity(oneBD.scaleByPowerOfTen(3), this, Some("kilo" + _name.getOrElse("")), Some("K" + symbol.getOrElse("")))
    //    def mega() = quantity(oneBD.scaleByPowerOfTen(6), this, Some("mega" + _name.getOrElse("")), Some("M" + symbol.getOrElse("")))
    //    def giga() = quantity(oneBD.scaleByPowerOfTen(9), this, Some("giga" + _name.getOrElse("")), Some("G" + symbol.getOrElse("")))
    //    def tera() = quantity(oneBD.scaleByPowerOfTen(12), this, Some("kilo" + _name.getOrElse("")), Some("T" + symbol.getOrElse("")))
    //    def peta() = quantity(oneBD.scaleByPowerOfTen(15), this, Some("peta" + _name.getOrElse("")), Some("P" + symbol.getOrElse("")))
    //    def exa() = quantity(oneBD.scaleByPowerOfTen(18), this, Some("exa" + _name.getOrElse("")), Some("E" + symbol.getOrElse("")))
    //    def zetta() = quantity(oneBD.scaleByPowerOfTen(21), this, Some("zetta" + _name.getOrElse("")), Some("Z" + symbol.getOrElse("")))
    //    def yotta() = quantity(oneBD.scaleByPowerOfTen(24), this, Some("yotta" + _name.getOrElse("")), Some("Y" + symbol.getOrElse("")))
    //    def deci() = quantity(oneBD.scaleByPowerOfTen(-1), this, Some("deci" + _name.getOrElse("")), Some("d" + symbol.getOrElse("")))
    //    def centi() = quantity(oneBD.scaleByPowerOfTen(-2), this, Some("centi" + _name.getOrElse("")), Some("c" + symbol.getOrElse("")))
    //    def milli() = quantity(oneBD.scaleByPowerOfTen(-3), this, Some("milli" + _name.getOrElse("")), Some("m" + symbol.getOrElse("")))
    //    def micro() = quantity(oneBD.scaleByPowerOfTen(-6), this, Some("micro" + _name.getOrElse("")), Some("μ" + symbol.getOrElse("")))
    //    def nano() = quantity(oneBD.scaleByPowerOfTen(-9), this, Some("nano" + _name.getOrElse("")), Some("n" + symbol.getOrElse("")))

    override def toString() = _name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + this.getClass().getSimpleName()

    def *:(bd: BigDecimal) = bd.doubleValue match {
      case 0.0 => zero()
      case _ => quantity(bd, this)
    }

    def in_:(bd: BigDecimal) = quantity(bd, this)

    def in(other: UOM): Q =
      conversionGraph.shortestPath(vertexFor(other), vertexFor(this)).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: DirectedGraphEdge[UOM, BigDecimal]) => bd.multiply(edge.payload))
      })
        .map(quantity(_, other))
        .getOrElse(throw new Exception("no conversion path from " + this + " to " + other))

  }

  def zero(): Q

  def newQuantity(magnitude: BigDecimal, unit: UOM): Q

  def quantity(magnitude: BigDecimal, unit: UOM): Q = newQuantity(magnitude, unit)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(Some(name), Some(symbol), linkOpt)

  def derive(compoundUnit: UnitOfMeasurement,
    nameOpt: Option[String] = None,
    symbolOpt: Option[String] = None,
    linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(nameOpt, symbolOpt, linkOpt)

  val uom2vertex = Map[UOM, JungDirectedGraphVertex[UOM]]()

  def vertexFor(uom: UOM): JungDirectedGraphVertex[UOM] = uom2vertex(uom)

  val wikipediaUrl: String

  //  val derivations: List[Quantum]
  //  def by(right: Quantum, resultQuantum: Quantum): Quantum = QuantumMultiplication(this, right, resultQuantum)
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
