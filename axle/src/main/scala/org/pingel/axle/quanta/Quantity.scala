package org.pingel.axle.quanta

import java.math.BigDecimal

object Scalar {
  implicit def toScalar(s: String) = Scalar(new BigDecimal(s))
}

case class Scalar(bd: BigDecimal) {
  implicit def in(uom: UnitOfMeasurement) = Quantity(bd, uom)
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

  override def toString() = magnitude + " " + unit.symbol

  def convert(conversion: Quantity): Quantity = {
    if( this.unit != conversion ) {
      throw new Exception("can't apply conversion " + conversion + " to " + this)
    }
    Quantity(magnitude.multiply(conversion.magnitude), conversion.unit)
  }
  
  implicit def in(other: UnitOfMeasurement): Option[Quantity] = {
    if (unit.quantum != other.quantum) {
      throw new Exception("incompatible quanta: " + unit.quantum + " and " + other.quantum)
    }
    unit.quantum.path(unit, other).map( _.foldLeft(this)( (q: Quantity, conversion: Quantity) => q.convert(conversion) ) )
  }
  
}
