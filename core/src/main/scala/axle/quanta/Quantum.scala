package axle.quanta

import axle.quanta._
import axle.graph._
import spire.math._
import spire.implicits._
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

trait QuantumExpression {

  def *(other: QuantumExpression): QuantumExpression = QuantumMultiplication(this, other)

  def /(other: QuantumExpression): QuantumExpression = QuantumDivision(this, other)

}

case class QuantumMultiplication(left: QuantumExpression, right: QuantumExpression) extends QuantumExpression
case class QuantumDivision(left: QuantumExpression, right: QuantumExpression) extends QuantumExpression

// case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum

/**
 * case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum
 */

trait Quantum extends QuantumExpression {

  quantum =>

  type Q <: Quantity

  def conversionGraph(): DirectedGraph[Q, Number => Number]

  def conversions(vps: Seq[Q], ef: Seq[Vertex[Q]] => Seq[(Vertex[Q], Vertex[Q], Number => Number)]): DirectedGraph[Q, Number => Number] =
    JungDirectedGraph(vps, ef)

  def trips2fns(trips: Seq[(Vertex[Q], Vertex[Q], Number)]) = trips.flatMap(trip2fns(_))

  def trip2fns(trip: (Vertex[Q], Vertex[Q], Number)): Seq[(Vertex[Q], Vertex[Q], Number => Number)] =
    Vector(
      (trip._1, trip._2, x => x * trip._3),
      (trip._2, trip._1, x => x / trip._3)
    )

  val one = Number.one

  def byName(unitName: String): Q = conversionGraph.findVertex(_.payload.name == unitName).get.payload

  def is(qe: QuantumExpression) = 4

  class Quantity(
    magnitude: Number = Number(1),
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) {

    self: Q =>

    type QUA = quantum.type

    def +(right: Q): Q =
      quantity((this in right.unit).magnitude + right.magnitude, right.unit)

    def -(right: Q): Q =
      quantity((this in right.unit).magnitude - right.magnitude, right.unit)

    def *(n: Number): Q = quantity(magnitude * n, unit)

    def /(n: Number): Q = quantity(magnitude / n, unit)

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(magnitude * right.magnitude, resultQuantum.newUnitOfMeasurement(None, None, None))

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(magnitude / bottom.magnitude, resultQuantum.newUnitOfMeasurement(None, None, None))

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def magnitude(): Number = magnitude
    def unit() = _unit.getOrElse(this)
    def name() = _name.getOrElse("")
    def label() = _name.getOrElse("")
    def symbol() = _symbol
    def link() = _link

    def vertex() = quantum.conversionGraph.findVertex(_.payload == this).get

    override def toString() =
      if (_unit.isDefined)
        magnitude.toString + unit.symbol.map(" " + _).getOrElse("")
      else
        _name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + getClass().getSimpleName()

    def *:(n: Number) = quantity(magnitude * n, this)

    def in_:(n: Number) = quantity(n, this)

    def in(other: Q): Q =
      conversionGraph.shortestPath(other.unit.vertex, unit.vertex)
        .map(
          _.map(_.payload).foldLeft(Number(1))((n, convert) => convert(n))
        )
        .map(n => quantity((magnitude * n) / other.magnitude, other))
        .getOrElse(throw new Exception("no conversion path from " + this + " to " + other))

  }

  def newQuantity(magnitude: Number, unit: Q): Q

  def quantity(magnitude: Number, unit: Q): Q = newQuantity(magnitude, unit)

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
    newUnitOfMeasurement(
      if (nameOpt.isDefined) nameOpt else Some(compoundUnit.unit.name),
      if (symbolOpt.isDefined) symbolOpt else compoundUnit.unit.symbol,
      linkOpt)

  val wikipediaUrl: String

  override def toString() = getClass().getSimpleName()

  import axle.visualize.Plottable

  case class UnitPlottable(base: quantum.Q) extends Plottable[quantum.Q] {

    import math.{ pow, ceil, floor, log10 }

    def isPlottable(t: quantum.Q): Boolean = true

    def zero() = Number(1) *: base

    def compare(u1: quantum.Q, u2: quantum.Q) = {
      val m1 = (u1 in base).magnitude
      val m2 = (u2 in base).magnitude
      if (m1 == m2) 0
      else if (m1 < m2) 1
      else -1
    }

    def portion(left: quantum.Q, v: quantum.Q, right: quantum.Q): Double =
      (((v in base).magnitude - (left in base).magnitude) / ((right in base).magnitude - (left in base).magnitude)).toDouble

    def step(from: Number, to: Number): Number =
      Number(10) ** ((log10((to - from).abs.toDouble)).floor)

    import Stream.{ empty, cons }

    def ticValueStream(v: Number, to: Number, step: Number): Stream[Number] =
      if (v > to) empty else cons(v, ticValueStream(v + step, to, step))

    def tics(from: quantum.Q, to: quantum.Q): Seq[(quantum.Q, String)] = {
      val fromD = (from in base).magnitude
      val toD = (to in base).magnitude
      val s = step(fromD, toD)
      val n = ((toD - fromD) / s).ceil.toInt
      val start = s * ((fromD / s).floor)
      (0 to n).map(i => {
        val v = start + (s * i)
        (v *: base, v.toString)
      }) // TODO filter(vs => (vs._1 >= fromD && vs._1 <= toD))
    }

  }

}


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
