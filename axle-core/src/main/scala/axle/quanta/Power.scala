package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Power() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

}

trait PowerMetadata[N] extends QuantumMetadata[Power, N] {

  type U = UnitOfMeasurement[Power, N]

  def watt: U
  def W: U
  def kilowatt: U
  def kW: U
  def megawatt: U
  def MW: U
  def gigawatt: U
  def GW: U
  def milliwatt: U
  def horsepower: U
  def lightBulb: U
  def hooverDam: U
  def mustangGT: U
}

object Power {

  def metadata[N] = new PowerMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Power, N](name, symbol, wiki)

    lazy val _watt = unit("watt", "W")
    lazy val _kilowatt = unit("kilowatt", "KW")
    lazy val _megawatt = unit("megawatt", "MW")
    lazy val _gigawatt = unit("gigawatt", "GW")
    lazy val _milliwatt = unit("milliwatt", "mW")
    lazy val _horsepower = unit("horsepower", "hp")
    lazy val _lightBulb = unit("light bulb", "light bulb")
    lazy val _hooverDam = unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
    lazy val _mustangGT = unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

    def watt = _watt
    def W = _watt
    def kilowatt = _kilowatt
    def kW = _kilowatt
    def megawatt = _megawatt
    def MW = _megawatt
    def gigawatt = _gigawatt
    def GW = _gigawatt
    def milliwatt = _milliwatt
    def horsepower = _horsepower
    def lightBulb = _lightBulb
    def hooverDam = _hooverDam
    def mustangGT = _mustangGT

    def units: List[UnitOfMeasurement[Power, N]] =
      List(watt, kilowatt, megawatt, gigawatt, milliwatt, horsepower, lightBulb, hooverDam, mustangGT)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Power, N], UnitOfMeasurement[Power, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Power, N], UnitOfMeasurement[Power, N], Bijection[N, N])](
        (watt, kilowatt, Scale10s(3)),
        (kilowatt, megawatt, Scale10s(3)),
        (megawatt, gigawatt, Scale10s(3)),
        (milliwatt, watt, Scale10s(3)),
        (watt, lightBulb, ScaleInt(60)),
        (megawatt, hooverDam, ScaleInt(2080)),
        (horsepower, mustangGT, ScaleInt(420)))

  }

}