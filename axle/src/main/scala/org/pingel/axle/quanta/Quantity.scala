package org.pingel.axle.quanta

import java.math.BigDecimal

import org.pingel.axle.graph._

object Scalar {
  implicit def toScalar(s: String) = Scalar(new BigDecimal(s))
}

case class Scalar(bd: BigDecimal) {
  implicit def in(uom: UnitOfMeasurement) = Quantity(bd, uom)
}

case class Conversion(cbd: BigDecimal, from: UnitOfMeasurement, to: UnitOfMeasurement)
extends Scalar(cbd)
with DirectedGraphEdge[UnitOfMeasurement] {
  def getVertices() = (from, to)
  def getSource() = from
  def getDest() = to
}

object Quantity {
  implicit def toBD(s: String) = new BigDecimal(s)
  implicit def toQuantity(sc: Scalar)(implicit uom: UnitOfMeasurement) = Quantity(sc.bd, uom)
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

  unit.quantum.addVertex(this)
  unit.quantum.addEdge(Conversion(magnitude, unit, this))
  
  override def toString() = magnitude + " " + unit.symbol

  def convert(conversion: Conversion): Quantity = {
    if (this.unit != conversion.from) {
      throw new Exception("can't apply conversion " + conversion + " to " + this)
    }
    Quantity(magnitude.multiply(conversion.cbd), conversion.to)
  }

  implicit def in(other: UnitOfMeasurement): Option[Quantity] = {
    if (unit.quantum != other.quantum) {
      throw new Exception("incompatible quanta: " + unit.quantum + " and " + other.quantum)
    }
    unit.quantum.conversionPath(unit, other).map(_.foldLeft(this)(
        (q: Quantity, conversion: Conversion) => q.convert(conversion)))
  }

}
