package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Power[N]() extends Quantum4[N] {

  type Q = Power[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def watt: UnitOfMeasurement4[Q, N] = unit("watt", "W")
  def W = watt
  def kilowatt: UnitOfMeasurement4[Q, N] = unit("kilowatt", "KW")
  def kW = kilowatt
  def megawatt: UnitOfMeasurement4[Q, N] = unit("megawatt", "MW")
  def MW = megawatt
  def gigawatt: UnitOfMeasurement4[Q, N] = unit("gigawatt", "GW")
  def GW = gigawatt
  def milliwatt: UnitOfMeasurement4[Q, N] = unit("milliwatt", "mW")
  def horsepower: UnitOfMeasurement4[Q, N] = unit("horsepower", "hp")
  def lightBulb: UnitOfMeasurement4[Q, N] = unit("light bulb", "light bulb")
  def hooverDam: UnitOfMeasurement4[Q, N] = unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
  def mustangGT: UnitOfMeasurement4[Q, N] = unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(watt, kilowatt, megawatt, gigawatt, milliwatt, horsepower, lightBulb, hooverDam, mustangGT)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (watt, kilowatt, Scale10s(3)),
      (kilowatt, megawatt, Scale10s(3)),
      (megawatt, gigawatt, Scale10s(3)),
      (milliwatt, watt, Scale10s(3)),
      (watt, lightBulb, ScaleInt(60)),
      (megawatt, hooverDam, ScaleInt(2080)),
      (horsepower, mustangGT, ScaleInt(420)))

}
