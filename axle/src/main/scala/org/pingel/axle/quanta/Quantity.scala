package org.pingel.axle.quanta

case class Quantity(
  magnitude: Double,
  unit: UnitOfMeasurement,
  name: Option[String] = None,
  link: Option[String] = None)
