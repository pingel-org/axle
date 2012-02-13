package org.pingel.axle.quanta

import org.pingel.axle.graph._

case class UnitOfMeasurement(quantum: Quantum, name: String, symbol: String, link: Option[String] = None)
  extends DirectedGraphVertex[Conversion] {
  
  import Quantity._

  def getLabel() = name

  def *(right: UnitOfMeasurement) = UomMultiplication(this, right)

  def /(right: UnitOfMeasurement) = UomDivision(this, right)

  def squared() = UomMultiplication(this, this)

  def kilo() = Quantity("1000", this, Some("kilo" + name), Some("K" + symbol)) // 3
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