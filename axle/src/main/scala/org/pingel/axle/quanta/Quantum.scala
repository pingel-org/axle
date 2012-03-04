package org.pingel.axle.quanta

import org.pingel.axle.graph._

import java.math.BigDecimal

case class UnitOfMeasurement(
  quantum: Quantum,
  name: String,
  symbol: String,
  link: Option[String] = None) {

  import Scalar._
  import Quantity._

  def getLabel() = name

  def *(right: UnitOfMeasurement) = UomMultiplication(this, right)

  def /(right: UnitOfMeasurement) = UomDivision(this, right)

  def squared() = UomMultiplication(this, this)

  def cubed() = UomMultiplication(this, UomMultiplication(this, this))

  // TODO: why is this toBD necessary?  figure out search order for implicits
  def kilo() = Quantity(toBD("1000"), this, Some("kilo" + name), Some("K" + symbol)) // 3
  def mega() = Quantity("1000", kilo, Some("mega" + name), Some("M" + symbol)) // 6
  def giga() = Quantity("1000", mega, Some("giga" + name), Some("G" + symbol)) // 9
  def tera() = Quantity("1000", giga, Some("kilo" + name), Some("T" + symbol)) // 12
  def peta() = Quantity("1000", tera, Some("peta" + name), Some("P" + symbol)) // 15
  def exa() = Quantity("1000", peta, Some("exa" + name), Some("E" + symbol)) // 18
  def zetta() = Quantity("1000", exa, Some("zetta" + name), Some("Z" + symbol)) // 21
  def yotta() = Quantity("1000", zetta, Some("yotta" + name), Some("Y" + symbol)) // 24

  def deci() = Quantity("0.1", this, Some("deci" + name), Some("d" + symbol)) // -1
  def centi() = Quantity("0.01", this, Some("centi" + name), Some("c" + symbol)) // -2
  def milli() = Quantity("0.001", this, Some("milli" + name), Some("m" + symbol)) // -3
  def micro() = Quantity("0.001", milli, Some("micro" + name), Some("μ" + symbol)) // -6
  def nano() = Quantity("0.001", micro, Some("nano" + name), Some("n" + symbol)) // -9

  override def toString() = name + " (" + symbol + "): a measure of " + quantum

}

import java.math.BigDecimal

object Scalar {
  implicit def toScalar(s: String) = new ScalarImpl(new BigDecimal(s))
}

trait Scalar {
  def getBD(): BigDecimal
  def in(uom: UnitOfMeasurement) = Quantity(getBD, uom)
}

case class ScalarImpl(bd: BigDecimal) {
  // Note: this used to be implicit
  def in(uom: UnitOfMeasurement) = Quantity(bd, uom)
}

case class UomMultiplication(left: UnitOfMeasurement, right: UnitOfMeasurement)
  extends UnitOfMeasurement(
    left.quantum * right.quantum,
    left.name + " " + right.name,
    left.symbol + right.symbol,
    None
  )

case class UomDivision(numerator: UnitOfMeasurement, denominator: UnitOfMeasurement)
  extends UnitOfMeasurement(
    numerator.quantum / denominator.quantum,
    numerator.name + " per " + denominator.name,
    numerator.symbol + "/" + denominator.symbol,
    None
  ) {

}

trait Conversion extends Scalar {
  def getFrom(): UnitOfMeasurement
  def getTo(): UnitOfMeasurement
}

case class ConversionImpl(from: UnitOfMeasurement, to: UnitOfMeasurement, cbd: BigDecimal)
  extends Conversion {
  def getBD() = cbd
  def getFrom(): UnitOfMeasurement = from
  def getTo(): UnitOfMeasurement = to
}

object Quantity {
  implicit def toBD(s: String) = new BigDecimal(s)
  implicit def toQuantity(sc: Scalar)(implicit uom: UnitOfMeasurement): Quantity = Quantity(sc.getBD, uom)
}

case class Quantity(
  magnitude: BigDecimal,
  unit: UnitOfMeasurement,
  qname: Option[String] = None,
  qsymbol: Option[String] = None,
  qlink: Option[String] = None)
  extends UnitOfMeasurement(
    unit.quantum,
    qname.getOrElse("?"),
    qsymbol.getOrElse("?"),
    qlink
  ) {

  unit.quantum.addQuantity(this)

  override def toString() = magnitude + " " + unit.symbol

  def +(right: Quantity): Quantity = Quantity(magnitude.add((right in unit).magnitude), unit)

  def -(right: Quantity): Quantity = Quantity(magnitude.subtract((right in unit).magnitude), unit)

  def *(right: Quantity): Quantity = Quantity(magnitude.multiply(right.magnitude), unit * right.unit)

  def /(right: Quantity): Quantity = Quantity(
    magnitude.divide(right.magnitude,
      scala.Math.max(magnitude.precision, right.magnitude.precision),
      java.math.RoundingMode.HALF_UP),
    unit / right.unit
  )

  def convert(conversion: Conversion): Quantity = {
    if (this.unit != conversion.getFrom()) {
      throw new Exception("can't apply conversion " + conversion + " to " + this)
    }
    Quantity(magnitude.multiply(conversion.getBD()), conversion.getTo())
  }

  implicit def in(other: UnitOfMeasurement): Quantity = {
    if (unit.quantum != other.quantum) {
      throw new Exception("incompatible quanta: " + unit.quantum + " and " + other.quantum)
    }
    val result = unit.quantum.conversionPath(unit, other).map(_.foldLeft(this)(
      (q: Quantity, conversion: Conversion) => q.convert(conversion)))
    if (result.isEmpty) {
      throw new Exception("no conversion path from " + unit + " to " + other)
    }
    result.get
  }

}

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

  case class UnitOfMeasurementAsVertex(vquantum: Quantum, vname: String, vsymbol: String, vlink: Option[String] = None)
    extends UnitOfMeasurement(vquantum, vname, vsymbol, vlink)
    with DirectedGraphVertex {

  }

  case class ConversionAsEdge(efrom: UnitOfMeasurementAsVertex, eto: UnitOfMeasurementAsVertex, ecbd: BigDecimal)
    extends ConversionImpl(efrom, eto, ecbd)
    with DirectedGraphEdge {
    def getVertices() = (efrom, eto)
    def getSource() = efrom
    def getDest() = eto
  }

  type V = UnitOfMeasurementAsVertex
  
  type E = ConversionAsEdge

  val one = new BigDecimal("1")
  
  def addQuantity(q: Quantity): Unit = {
    val qv = q.asInstanceOf[UnitOfMeasurementAsVertex] // TODO remove cast
    val uv = q.unit.asInstanceOf[UnitOfMeasurementAsVertex] // TODO remove cast
    addVertex(qv)
    newEdge(qv, uv, q.magnitude)
    newEdge(uv, qv, one.divide(q.magnitude, q.magnitude.precision, java.math.RoundingMode.HALF_UP))
  }

  def newVertex(label: String): UnitOfMeasurementAsVertex = {
    null // TODO
  }
  
  def newEdge(source: UnitOfMeasurementAsVertex, dest: UnitOfMeasurementAsVertex): ConversionAsEdge = {
    null // TODO
  }
  
  def newEdge(source: UnitOfMeasurementAsVertex, dest: UnitOfMeasurementAsVertex, magnitude: BigDecimal): ConversionAsEdge = {
    val edge = new ConversionAsEdge(source, dest, magnitude)
    addEdge(edge)
    edge
  }

  val wikipediaUrl: String

  val derivations: List[Quantum]

  def *(right: Quantum): Quantum = QuantumMultiplication(this, right)
  def /(right: Quantum): Quantum = QuantumMultiplication(this, right)
  def squared(): Quantum = QuantumMultiplication(this, this)
  def cubed(): Quantum = QuantumMultiplication(this, QuantumMultiplication(this, this))

  override def toString() = this.getClass().getSimpleName()

  /**
   * Searches the Directed Graph defined by this Quantum for a path of Conversions from source to goal
   * @param source Start node for shortest path search
   * @param goal End node for shortest path search
   */
  def conversionPath(source: UnitOfMeasurement, goal: UnitOfMeasurement): Option[List[Conversion]] =
    shortestPath(
      source.asInstanceOf[V],
      goal.asInstanceOf[V])
      // .asInstanceOf[Option[List[Conversion]]] // TODO: remove cast

}

case class QuantumMultiplication(left: Quantum, right: Quantum) extends Quantum {
  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil // TODO multiplications of the cross-product of left and right
  val derivations = Nil
  val examples = Nil
}

case class QuantumDivision(left: Quantum, right: Quantum) extends Quantum {
  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil // TODO divisions of the cross-product of left and right
  val derivations = Nil
  val examples = Nil
}
