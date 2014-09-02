package axle.quanta

import axle.quanta._
import axle.graph._
import spire.math._
import spire.algebra._
import spire.implicits._
import math.{ max, abs }
import axle.graph.JungDirectedGraph

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

abstract class Quantum[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends QuantumExpression {

  quantum =>

  type Q <: Quantity
  
  def field = implicitly[Field[N]]

  implicit def eqTypeclass: Eq[Q]

  // Note: this is need for "def conversions"
  implicit def edgeEq: Eq[N => N] = new Eq[N => N] {
    def eqv(x: N => N, y: N => N): Boolean = ???
  }

  def conversionGraph: DirectedGraph[Q, N => N]

  def conversions(vps: Seq[Q], ef: Seq[Vertex[Q]] => Seq[(Vertex[Q], Vertex[Q], N => N)]): DirectedGraph[Q, N => N] =
    JungDirectedGraph(vps, ef)

  private[quanta] def trips2fns(trips: Seq[(Vertex[Q], Vertex[Q], N)]) = trips.flatMap(trip2fns)

  private[quanta] def trip2fns(trip: (Vertex[Q], Vertex[Q], N)): Seq[(Vertex[Q], Vertex[Q], N => N)] = {
    val (from, to, multiplier) = trip
    Vector(
      (from, to, _ * multiplier),
      (to, from, _ / multiplier))
  }

  def byName(unitName: String): Q = conversionGraph.findVertex(_.payload.name === unitName).get.payload

  def is(qe: QuantumExpression): Boolean = ??? // TODO

  object Quantity {

    implicit def eqQuantity: Eq[Quantity] = new Eq[Quantity] {
      def eqv(x: Quantity, y: Quantity): Boolean =
        (x.magnitude === y.magnitude) &&
          (if (x.unitOption.isDefined && y.unitOption.isDefined) {
            (x.unitOption.get === y.unitOption.get)
          } else {
            false // was super.equals(otherQuantity)
          })
    }

    //    override def equals(other: Any): Boolean = other match {
    //      case otherQuantity: Quantity =>
    //        (magnitude === otherQuantity.magnitude) &&
    //          (if (_unit.isDefined && otherQuantity.unitOption.isDefined) {
    //            (_unit.get === otherQuantity.unitOption.get)
    //          } else {
    //            super.equals(otherQuantity)
    //          })
    //      case _ => false
    //    }

    // override def hashCode: Int = this._magnitude.## + this._unit.##

  }

  class Quantity(
    _magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) {

    self: Q =>

    def +(right: Q): Q =
      quantity((this in right.unit).magnitude + right.magnitude, right.unit)

    def -(right: Q): Q =
      quantity((this in right.unit).magnitude - right.magnitude, right.unit)

    def *(n: N): Q = quantity(_magnitude * n, unit)

    def /(n: N): Q = quantity(_magnitude / n, unit)

    def <(other: Q): Boolean = (other - this).magnitude > 0

    def >(other: Q): Boolean = (other - this).magnitude < 0

    def by[QRGT <: Quantum[N], QRES <: Quantum[N]](right: QRGT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(_magnitude * right.magnitude, resultQuantum.newUnitOfMeasurement(None, None, None))

    def over[QBOT <: Quantum[N], QRES <: Quantum[N]](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q =
      resultQuantum.quantity(_magnitude / bottom.magnitude, resultQuantum.newUnitOfMeasurement(None, None, None))

    def through[QBOT <: Quantum[N], QRES <: Quantum[N]](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def per[QBOT <: Quantum[N], QRES <: Quantum[N]](bottom: QBOT#Q, resultQuantum: QRES): QRES#Q = over(bottom, resultQuantum)

    def magnitude: N = _magnitude

    def unitOption: Option[Q] = _unit

    def unit: Q = _unit.getOrElse(this)

    def name: String = _name.getOrElse("")

    def label: String = _name.getOrElse("")

    def symbol: Option[String] = _symbol

    def link: Option[String] = _link

    def vertex: Vertex[Q] = quantum.conversionGraph.findVertex(_.payload === this).get

    override def toString: String =
      if (_unit.isDefined) {
        magnitude.toString + unit.symbol.map(" " + _).getOrElse("")
      } else {
        _name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + getClass.getSimpleName
      }

    // (implicit module: Module[N, Rational])
    def *:(n: N): Q = quantity(magnitude * n, this)
    
    def in_:(n: N): Q = quantity(n, this)

    def in(other: Q): Q =
      conversionGraph.shortestPath(other.unit.vertex, unit.vertex)
        .map(
          _.map(_.payload).foldLeft(field.one)((n, convert) => convert(n)))
        .map(n => quantity((magnitude * n) / other.magnitude, other))
        .getOrElse(throw new Exception("no conversion path from " + this + " to " + other))

    def plottable: UnitPlottable = quantum.UnitPlottable(this)(space)

  }

  def newQuantity(magnitude: N, unit: Q): Q

  def quantity(magnitude: N, unit: Q): Q = newQuantity(magnitude, unit)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): Q

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): Q =
    newUnitOfMeasurement(Some(name), Some(symbol), linkOpt)

  def derive[N](compoundUnit: Q,
    nameOpt: Option[String] = None,
    symbolOpt: Option[String] = None,
    linkOpt: Option[String] = None): Q =
    newUnitOfMeasurement(
      if (nameOpt.isDefined) nameOpt else Some(compoundUnit.unit.name),
      if (symbolOpt.isDefined) symbolOpt else compoundUnit.unit.symbol,
      linkOpt)

  val wikipediaUrl: String

  override def toString: String = getClass.getSimpleName

  import axle.algebra.Plottable

  case class UnitPlottable(base: quantum.Q)(implicit space: MetricSpace[N, Double]) extends Plottable[quantum.Q] {

    val underlying = Plottable.abstractAlgebraPlottable[N]

    import math.{ pow, ceil, floor, log10 }
    import Stream.{ empty, cons }

    def isPlottable(t: quantum.Q): Boolean = underlying.isPlottable(t.magnitude)

    def zero: Q = field.zero *: base

    def compare(u1: quantum.Q, u2: quantum.Q): Int = underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)

    def portion(left: quantum.Q, v: quantum.Q, right: quantum.Q): Double =
      underlying.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

    def tics(from: quantum.Q, to: quantum.Q): Seq[(quantum.Q, String)] =
      underlying.tics((from in base).magnitude, (to in base).magnitude) map {
        case (v, label) =>
          (v *: base, v.toString)
      }

  }

}

// 3 kilo 6 mega 9 giga 12 tera 15 peta 18 exa 21 zetta 24 yotta

