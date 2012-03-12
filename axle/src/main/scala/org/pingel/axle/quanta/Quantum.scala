package org.pingel.axle.quanta

import org.pingel.axle.graph._
import java.math.BigDecimal
import scala.Math.{max, abs}

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

trait Quantum extends DirectedGraph {

  outer =>

  type UOM <: UnitOfMeasurement

  implicit def toBD(i: Int) = new BigDecimal(i.toString)

  implicit def toBD(d: Double) = new BigDecimal(d.toString)

  implicit def toBD(s: String) = new BigDecimal(s)

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) =
    numerator.divide(
      denominator,
      max(max(numerator.precision, abs(numerator.scale)),
          max(denominator.precision, abs(denominator.scale))),
      java.math.RoundingMode.HALF_UP)

  case class Conversion(from: UOM, to: UOM, bd: BigDecimal) extends DirectedGraphEdge {

    def getVertices() = (from, to)
    def getSource() = from
    def getDest() = to
    def getBD() = bd
    def getLabel() = bd.toString

    override def toString() = from.toString() + " * " + bd + " = " + to.toString()
  }

  val one = new BigDecimal("1")

  class UnitOfMeasurement(
    var conversion: Option[Conversion],
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends DirectedGraphVertex {

    self: UOM =>

    def getLabel() = name.getOrElse("")
    def getSymbol() = symbol
    def getLink() = link

    val quantum: Quantum = outer

    def kilo() = quantity(one.scaleByPowerOfTen(3), this, Some("kilo" + name.getOrElse("")), Some("K" + symbol.getOrElse("")))
    def mega() = quantity(one.scaleByPowerOfTen(6), this, Some("mega" + name.getOrElse("")), Some("M" + symbol.getOrElse("")))
    def giga() = quantity(one.scaleByPowerOfTen(9), this, Some("giga" + name.getOrElse("")), Some("G" + symbol.getOrElse("")))
    def tera() = quantity(one.scaleByPowerOfTen(12), this, Some("kilo" + name.getOrElse("")), Some("T" + symbol.getOrElse("")))
    def peta() = quantity(one.scaleByPowerOfTen(15), this, Some("peta" + name.getOrElse("")), Some("P" + symbol.getOrElse("")))
    def exa() = quantity(one.scaleByPowerOfTen(18), this, Some("exa" + name.getOrElse("")), Some("E" + symbol.getOrElse("")))
    def zetta() = quantity(one.scaleByPowerOfTen(21), this, Some("zetta" + name.getOrElse("")), Some("Z" + symbol.getOrElse("")))
    def yotta() = quantity(one.scaleByPowerOfTen(24), this, Some("yotta" + name.getOrElse("")), Some("Y" + symbol.getOrElse("")))

    def deci() = quantity(one.scaleByPowerOfTen(-1), this, Some("deci" + name.getOrElse("")), Some("d" + symbol.getOrElse("")))
    def centi() = quantity(one.scaleByPowerOfTen(-2), this, Some("centi" + name.getOrElse("")), Some("c" + symbol.getOrElse("")))
    def milli() = quantity(one.scaleByPowerOfTen(-3), this, Some("milli" + name.getOrElse("")), Some("m" + symbol.getOrElse("")))
    def micro() = quantity(one.scaleByPowerOfTen(-6), this, Some("micro" + name.getOrElse("")), Some("μ" + symbol.getOrElse("")))
    def nano() = quantity(one.scaleByPowerOfTen(-9), this, Some("nano" + name.getOrElse("")), Some("n" + symbol.getOrElse("")))

    override def hashCode = 42 // TODO (was overflowing stack)

    override def toString() = conversion
      .map(c => c.bd + " " + c.getSource().getSymbol.getOrElse(""))
      .getOrElse(name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + this.getClass().getSimpleName())

    def *:(bd: BigDecimal) = quantity(bd, this)
    def in_:(bd: BigDecimal) = quantity(bd, this)

    def +(right: UOM): UOM = {
      val (bd, uom) = conversion.map(c => (c.bd, c.getSource)).getOrElse((one, this))
      quantity(bd.add((right in uom).conversion.get.bd), uom) // TODO remove .get
    }

    def -(right: UOM): UOM = {
      val (bd, uom) = conversion.map(c => (c.bd, c.getSource)).getOrElse((one, this))
      quantity(bd.subtract((right in uom).conversion.get.bd), uom) // TODO remove .get
    }

    def *(bd: BigDecimal): UOM = conversion
      .map(c => quantity(c.bd.multiply(bd), c.getSource))
      .getOrElse(quantity(bd, this))

    def /(bd: BigDecimal): UOM = conversion
      .map(c => quantity(bdDivide(c.bd, bd), c.getSource))
      .getOrElse(quantity(bdDivide(one, bd), this))

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = conversion.map(c =>
        right.conversion.map(rc => c.bd.multiply(rc.bd)
        ).getOrElse(c.bd)
      ).getOrElse(right.conversion.map(rc => rc.bd)
        .getOrElse(one)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = conversion.map(c =>
        bottom.conversion.map(bc => bdDivide(c.bd, bc.bd)
        ).getOrElse(c.bd)
      ).getOrElse(bottom.conversion.map(bc => bdDivide(one, bc.bd))
        .getOrElse(one)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def in(other: UOM): UOM = {
      val resultBD = conversionPath(other, this).map(path => {
        path.foldLeft(one)((bd: BigDecimal, conversion: Conversion) => bd.multiply(conversion.bd))
      })
      if (resultBD.isEmpty) {
        throw new Exception("no conversion path from " + this + " to " + other)
      }

      quantity(resultBD.get, other)
    }

  }

  type V = UOM

  type E = Conversion

  def newUnitOfMeasurement(
    conversion: Option[Conversion] = None,
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

  def quantity(
    magnitude: BigDecimal,
    unit: UOM,
    qname: Option[String] = None,
    qsymbol: Option[String] = None,
    qlink: Option[String] = None): UOM = {

    val q = newUnitOfMeasurement(None, qname, qsymbol, qlink)
    addVertex(q)
    val conversion1 = newEdge(q, unit, bdDivide(one, magnitude))
    val conversion2 = newEdge(unit, q, magnitude)
    q.conversion = Some(conversion2)
    q
  }

  def newVertex(label: String): UOM = newUnitOfMeasurement(None, Some(label), None, None)

  def newEdge(source: UOM, dest: UOM): Conversion = {
    val result: Conversion = null
    result
  }

  def newEdge(source: UOM, dest: UOM, magnitude: BigDecimal): Conversion = {
    val edge = new Conversion(source, dest, magnitude)
    addEdge(edge)
    edge
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
  def conversionPath(source: UOM, goal: UOM): Option[List[Conversion]] =
    shortestPath(
      source.asInstanceOf[V],
      goal.asInstanceOf[V])

}

//case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum {
//
//  type UOM = QRESULT#UOM
//
//  val wikipediaUrl = ""
//  val unitsOfMeasurement = Nil
//  val derivations = Nil
//  val examples = Nil
//  
//  def newUnitOfMeasurement(
//    baseUnit: Option[QRESULT#UOM] = None,
//    magnitude: BigDecimal,
//    name: Option[String] = None,
//    symbol: Option[String] = None,
//    link: Option[String] = None): UOM = {
//
//    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
//  }
//  
//}
//
//case class QuantumDivision[QTOP <: Quantum, QBOTTOM <: Quantum, QRESULT <: Quantum](top: QTOP, bottom: QBOTTOM, resultQuantum: QRESULT) extends Quantum {
//
//  type UOM = QRESULT#UOM
//
//  val wikipediaUrl = ""
//  val unitsOfMeasurement = Nil
//  val derivations = Nil
//  val examples = Nil
//  
//  def newUnitOfMeasurement(
//    baseUnit: Option[UOM] = None,
//    magnitude: BigDecimal,
//    name: Option[String] = None,
//    symbol: Option[String] = None,
//    link: Option[String] = None): UOM = {
//
//    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
//  }
//  
//}
