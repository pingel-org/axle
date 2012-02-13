package org.pingel.axle.quanta

case class Quantity(
  magnitude: Double,
  unit: UnitOfMeasurement,
  name: Option[String] = None,
  symbol: Option[String] = None,
  link: Option[String] = None)
  extends UnitOfMeasurement(
    unit.quantum,
    name.getOrElse("?"),
    name.getOrElse("?"),
    link
  )

