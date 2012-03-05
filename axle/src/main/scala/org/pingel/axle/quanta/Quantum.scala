package org.pingel.axle.quanta

import org.pingel.axle.graph._
import java.math.BigDecimal

/**
 * Quantum
 *
 * World English Dictionary
 *
 * 4. something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

trait Quantum extends DirectedGraph {

  type UOM <: UnitOfMeasurement

  implicit def toBD(s: String) = new BigDecimal(s)

  //  implicit def toUoM(bd: BigDecimal)(implicit uom: UnitOfMeasurement): UnitOfMeasurement =
  //    uom.quantum.quantity(bd, uom)
  //    
  //  implicit def in(uom: UnitOfMeasurement) = uom.quantum.quantity(bd, uom, None, None, None)

  case class Conversion(from: UOM, to: UOM, bd: BigDecimal) extends DirectedGraphEdge {
    def getVertices() = (from, to)
    def getSource() = from
    def getDest() = to
    def getBD() = bd
  }

  val one = new BigDecimal("1")

  case class UnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends DirectedGraphVertex {

    // self: UOM => // TODO: figure out how to use self-type
    val thisAsUOM = this.asInstanceOf[UOM]

    def getLabel() = name

    def kilo() = quantity("1000", thisAsUOM, Some("kilo" + name), Some("K" + symbol)) // 3
    def mega() = quantity("1000", kilo, Some("mega" + name), Some("M" + symbol)) // 6
    def giga() = quantity("1000", mega, Some("giga" + name), Some("G" + symbol)) // 9
    def tera() = quantity("1000", giga, Some("kilo" + name), Some("T" + symbol)) // 12
    def peta() = quantity("1000", tera, Some("peta" + name), Some("P" + symbol)) // 15
    def exa() = quantity("1000", peta, Some("exa" + name), Some("E" + symbol)) // 18
    def zetta() = quantity("1000", exa, Some("zetta" + name), Some("Z" + symbol)) // 21
    def yotta() = quantity("1000", zetta, Some("yotta" + name), Some("Y" + symbol)) // 24

    def deci() = quantity("0.1", thisAsUOM, Some("deci" + name), Some("d" + symbol)) // -1
    def centi() = quantity("0.01", thisAsUOM, Some("centi" + name), Some("c" + symbol)) // -2
    def milli() = quantity("0.001", thisAsUOM, Some("milli" + name), Some("m" + symbol)) // -3
    def micro() = quantity("0.001", milli, Some("micro" + name), Some("μ" + symbol)) // -6
    def nano() = quantity("0.001", micro, Some("nano" + name), Some("n" + symbol)) // -9

    override def toString() = baseUnit
      .map(u => magnitude + " " + u.symbol)
      .getOrElse(name + " (" + symbol + "): a measure of " + this.getClass().getSimpleName())

    def +(right: UOM): UOM =
      quantity(magnitude.add((right in baseUnit.getOrElse(thisAsUOM)).magnitude), baseUnit.getOrElse(thisAsUOM))

    def -(right: UOM): UOM =
      quantity(magnitude.subtract((right in baseUnit.getOrElse(thisAsUOM)).magnitude), baseUnit.getOrElse(thisAsUOM))

    def *(bd: BigDecimal): UOM =
      quantity(magnitude.multiply(bd), baseUnit.getOrElse(thisAsUOM))

    def /(bd: BigDecimal): UOM =
      quantity(magnitude.divide(bd, scala.Math.max(magnitude.precision, bd.precision), java.math.RoundingMode.HALF_UP), baseUnit.getOrElse(thisAsUOM))

    // TODO: use HList for by, over, squared, cubed


    // TODO: name, symbol, link for new units
      
    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = baseUnit match {
      case Some(base) => right.baseUnit match {
        case Some(rightBase) => resultQuantum.quantity(magnitude.multiply(right.magnitude), resultQuantum.unit("TODO", "TODO")) // base by rightBase
        case _ => resultQuantum.quantity(magnitude, resultQuantum.unit("TODO", "TODO")) // (base, right)
      }
      case _ => right.baseUnit match {
        case Some(rightBase) => resultQuantum.quantity(right.magnitude, resultQuantum.unit("TODO", "TODO")) // thisAsUOM by rightBase
        case _ => resultQuantum.quantity(one, resultQuantum.unit("TODO", "TODO")) // hisAsUOM by right
      }
    }

    def over[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = baseUnit match {

      case Some(base) => right.baseUnit match {

        case Some(rightBase) => resultQuantum.quantity(
          magnitude.divide(right.magnitude, scala.Math.max(magnitude.precision, right.magnitude.precision), java.math.RoundingMode.HALF_UP),
          resultQuantum.unit("TODO", "TODO")) // base over rightBase)

        case None => resultQuantum.quantity(magnitude, resultQuantum.unit("TODO", "TODO")) // base over right)

      }
      case None => right.baseUnit match {

        case Some(rightBase) => resultQuantum.quantity(
          one.divide(right.magnitude, right.magnitude.precision, java.math.RoundingMode.HALF_UP),
          resultQuantum.unit("TODO", "TODO")) // thisAsUOM over rightBase)

        case None => resultQuantum.quantity(one, resultQuantum.unit("TODO", "TODO")) // thisAsUOM over right)

      }
    }

    def convert(conversion: Conversion): UOM = {
      if (this != conversion.getSource()) {
        throw new Exception("can't apply conversion " + conversion + " to " + this)
      }
      quantity(magnitude.multiply(conversion.getBD()), conversion.getDest())
    }

    // Note: used to be implicit
    def in(other: UOM): UOM = {
      val result = conversionPath(thisAsUOM, other).map(_.foldLeft(thisAsUOM)(
        (q: UOM, conversion: Conversion) => q.convert(conversion)
      ))
      if (result.isEmpty) {
        throw new Exception("no conversion path from " + this + " to " + other)
      }
      result.get
    }

  }

  type V = UOM

  type E = Conversion

  def newUnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(None, one, Some(name), Some(symbol), linkOpt)

  def derive(compoundUnit: UnitOfMeasurement, nameOpt: Option[String] = None, symbolOpt: Option[String] = None, linkOpt: Option[String] = None): UOM = {
    // TODO: Check that the given compound unit is in this quantum's list of derivations
    // TODO: add the compoundUnit to the graph?
    newUnitOfMeasurement(None, one, nameOpt, symbolOpt, linkOpt)
  }

  def quantity(
    magnitude: BigDecimal,
    unit: UOM,
    qname: Option[String] = None,
    qsymbol: Option[String] = None,
    qlink: Option[String] = None): UOM = {

    val q = newUnitOfMeasurement(Some(unit), magnitude, qname, qsymbol, qlink)
    addVertex(q)
    newEdge(q, unit, magnitude)
    newEdge(unit, q, one.divide(magnitude, magnitude.precision, java.math.RoundingMode.HALF_UP))
    q
  }

  def newVertex(label: String): UOM = newUnitOfMeasurement(None, one, Some(label), None, None)

  def newEdge(source: UOM, dest: UOM): Conversion = {
    val result: Conversion = null // TODO
    result
  }

  def newEdge(source: UOM, dest: UOM, magnitude: BigDecimal): Conversion = {
    val edge = new Conversion(source, dest, magnitude)
    addEdge(edge)
    edge
  }

  val wikipediaUrl: String

  val derivations: List[Quantum]

  def by(right: Quantum): Quantum = QuantumMultiplication(this, right)

  def over(right: Quantum): Quantum = QuantumMultiplication(this, right)

  def squared(): Quantum = QuantumMultiplication(this, this)

  def cubed(): Quantum = QuantumMultiplication(this, QuantumMultiplication(this, this))

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

case class QuantumMultiplication(left: Quantum, right: Quantum) extends Quantum {

  type U = Int // (left.type#U, right.type#U) // TODO

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil // TODO multiplications of the cross-product of left and right
  val derivations = Nil
  val examples = Nil
}

case class QuantumDivision(left: Quantum, right: Quantum) extends Quantum {

  type U = Int // (left.type#U, right.type#U) // TODO

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil // TODO divisions of the cross-product of left and right
  val derivations = Nil
  val examples = Nil
}
