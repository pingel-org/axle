package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Power extends Quantum {

  type Q = Power.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def watt[N]: UnitOfMeasurement[Q, N] = unit("watt", "W")
  def W[N] = watt[N]
  def kilowatt[N]: UnitOfMeasurement[Q, N] = unit("kilowatt", "KW")
  def kW[N] = kilowatt[N]
  def megawatt[N]: UnitOfMeasurement[Q, N] = unit("megawatt", "MW")
  def MW[N] = megawatt[N]
  def gigawatt[N]: UnitOfMeasurement[Q, N] = unit("gigawatt", "GW")
  def GW[N] = gigawatt[N]
  def milliwatt[N]: UnitOfMeasurement[Q, N] = unit("milliwatt", "mW")
  def horsepower[N]: UnitOfMeasurement[Q, N] = unit("horsepower", "hp")
  def lightBulb[N]: UnitOfMeasurement[Q, N] = unit("light bulb", "light bulb")
  def hooverDam[N]: UnitOfMeasurement[Q, N] = unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
  def mustangGT[N]: UnitOfMeasurement[Q, N] = unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(watt, kilowatt, megawatt, gigawatt, milliwatt, horsepower, lightBulb, hooverDam, mustangGT)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (watt, kilowatt, Scale10s(3)),
      (kilowatt, megawatt, Scale10s(3)),
      (megawatt, gigawatt, Scale10s(3)),
      (milliwatt, watt, Scale10s(3)),
      (watt, lightBulb, ScaleInt(60)),
      (megawatt, hooverDam, ScaleInt(2080)),
      (horsepower, mustangGT, ScaleInt(420)))

}
