package org.pingel.axle.quanta

// TODO: a "unit of measurement" defines a Quantity, too

case class UnitOfMeasurement(quantum: Quantum, name: String, symbol: String) {

  def *(right: UnitOfMeasurement) = UomMultiplication(this, right)
  
  def /(right: UnitOfMeasurement) = UomDivision(this, right)
  
  def squared() = UomMultiplication(this, this)
  
  def kilo() = UnitOfMeasurement(quantum, "kilo" + name, "K" + symbol) // 3
  def mega() = UnitOfMeasurement(quantum, "kilo" + name, "M" + symbol) // 6
  def giga() = UnitOfMeasurement(quantum, "kilo" + name, "G" + symbol) // 9
  def tera() = UnitOfMeasurement(quantum, "kilo" + name, "T" + symbol) // 12
  def peta() = UnitOfMeasurement(quantum, "peta" + name, "P" + symbol) // 15
  def exa() = UnitOfMeasurement(quantum, "exa" + name, "E" + symbol) // 18
  def zetta() = UnitOfMeasurement(quantum, "zetta" + name, "Z" + symbol) // 21
  def yotta() = UnitOfMeasurement(quantum, "yotta" + name, "Y" + symbol) // 24

  def centi() = UnitOfMeasurement(quantum, "centi" + name, "c" + symbol) // -2
  def milli() = UnitOfMeasurement(quantum, "milli" + name, "m" + symbol) // -3
  def micro() = UnitOfMeasurement(quantum, "micro" + name, "μ" + symbol) // -6
  def nano() = UnitOfMeasurement(quantum, "nano" + name, "n" + symbol) // -9

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