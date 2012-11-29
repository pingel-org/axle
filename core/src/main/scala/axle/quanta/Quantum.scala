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

  def conversionGraph(): JungDirectedGraph[Q, BigDecimal]

  def byName(unitName: String): Q = conversionGraph.findVertex(_.payload.name == unitName).get.payload

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

  def withInverses(trips: Seq[(JungDirectedGraphVertex[Q], JungDirectedGraphVertex[Q], BigDecimal)]): Seq[(JungDirectedGraphVertex[Q], JungDirectedGraphVertex[Q], BigDecimal)] =
    trips.flatMap(trip => Vector(trip, (trip._2, trip._1, bdDivide(oneBD, trip._3))))

  class Quantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) {

    self: Q =>

    def +(right: Q): Q =
      quantity((this in right.unit).magnitude.add(right.magnitude), right.unit)

    def -(right: Q): Q =
      quantity((this in right.unit).magnitude.subtract(right.magnitude), right.unit)

    def *(bd: BigDecimal): Q = quantity(this.magnitude.multiply(bd), this.unit)

    def /(bd: BigDecimal): Q = quantity(bdDivide(this.magnitude, bd), this.unit)

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(this.magnitude.multiply(right.magnitude), resultQuantum.newUnitOfMeasurement(None, None, None))

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(bdDivide(this.magnitude, bottom.magnitude), resultQuantum.newUnitOfMeasurement(None, None, None))

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    val quantum: Quantum = outer

    def magnitude(): BigDecimal = magnitude
    def unit() = _unit.getOrElse(this)
    def name() = _name.getOrElse("")
    def label() = _name.getOrElse("")
    def symbol() = _symbol
    def link() = _link

    def vertex(): JungDirectedGraphVertex[Q] =
      quantum.conversionGraph.findVertex(_.payload == this).get.asInstanceOf[JungDirectedGraphVertex[Q]]

    override def toString() =
      if (_unit.isDefined)
        magnitude + unit.symbol.map(" " + _).getOrElse("")
      else
        _name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + this.getClass().getSimpleName()

    def *:(bd: BigDecimal) = quantity(this.magnitude.multiply(bd), this)

    def in_:(bd: BigDecimal) = quantity(bd, this)

    def in(other: Q): Q =
      conversionGraph.shortestPath(other.unit.vertex, this.unit.vertex).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: DirectedGraphEdge[Q, BigDecimal]) => bd.multiply(edge.payload))
      })
        .map(bd => quantity(bdDivide(this.magnitude.multiply(bd), other.magnitude), other))
        .getOrElse(throw new Exception("no conversion path from " + this + " to " + other))
  }

  def newQuantity(magnitude: BigDecimal, unit: Q): Q

  def quantity(magnitude: BigDecimal, unit: Q): Q = newQuantity(magnitude, unit)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): Q

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): Q =
    newUnitOfMeasurement(Some(name), Some(symbol), linkOpt)

  def derive(compoundUnit: Q,
    nameOpt: Option[String] = None,
    symbolOpt: Option[String] = None,
    linkOpt: Option[String] = None): Q =
    newUnitOfMeasurement(Some(compoundUnit.unit.name), compoundUnit.unit.symbol, linkOpt)

  val wikipediaUrl: String

  override def toString() = this.getClass().getSimpleName()

}

/**
case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum {

  type Q = QRESULT#Q

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[QRESULT#Q] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): Q = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}

case class QuantumDivision[QTOP <: Quantum, QBOTTOM <: Quantum, QRESULT <: Quantum](top: QTOP, bottom: QBOTTOM, resultQuantum: QRESULT) extends Quantum {

  type Q = QRESULT#Q

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[Q] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): Q = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}
*/

/**
  object ConversionGraphFactoryObject extends ConversionGraphFactory
  trait ConversionGraphFactory extends JungDirectedGraphFactory {
    
    type G = ConversionGraph
    
    trait ConversionGraph extends JungDirectedGraph[Q, BigDecimal] {

      type V = ConversionGraphVertex
      trait ConversionGraphVertex extends JungDirectedGraphVertex[Q]

      type E = ConversionGraphEdge
      trait ConversionGraphEdge extends JungDirectedGraphEdge[BigDecimal]
    }
  }
  import ConversionGraphFactoryObject._
 * 
 * 
 */

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
