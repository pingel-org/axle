package org.pingel.axle.quanta

// TODO: a "unit of measurement" defines a Quantity, too

case class UnitOfMeasurement(quantum: Quantum, name: String, symbol: String) {

  def *(right: UnitOfMeasurement) = UomMultiplication(this, right)
  
  def /(right: UnitOfMeasurement) = UomDivision(this, right)
  
  def squared() = UomMultiplication(this, this)
  
  def kilo() = UnitOfMeasurement(quantum, "kilo" + name, "k" + symbol)
  def mega() = UnitOfMeasurement(quantum, "kilo" + name, "M" + symbol)
  def giga() = UnitOfMeasurement(quantum, "kilo" + name, "g" + symbol)
  def tera() = UnitOfMeasurement(quantum, "kilo" + name, "t" + symbol)
  def peta() = UnitOfMeasurement(quantum, "peta" + name, "p" + symbol)
  def centi() = UnitOfMeasurement(quantum, "centi" + name, "c" + symbol)
  def milli() = UnitOfMeasurement(quantum, "milli" + name, "m" + symbol)
  def micro() = UnitOfMeasurement(quantum, "micro" + name, "μ" + symbol)
  def nano() = UnitOfMeasurement(quantum, "nano" + name, "n" + symbol)

}

case class UomMultiplication(left: UnitOfMeasurement, right: UnitOfMeasurement)
extends UnitOfMeasurement(
    left.quantum * right.quantum,
    left.name + " " + right.name,
    left.symbol + right.symbol
    )

case class UomDivision(numerator: UnitOfMeasurement, denominator: UnitOfMeasurement)
  extends UnitOfMeasurement(
    numerator.quantum / denominator.quantum,
    numerator.name + " per " + denominator.name,
    numerator.symbol + "/" + denominator.symbol
  ) {

}