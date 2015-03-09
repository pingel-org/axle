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

}

object Power {

  def metadata[N] = new PowerMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Power, N](name, symbol, wiki)

    lazy val watt = unit("watt", "W")
    lazy val W = watt
    lazy val kilowatt = unit("kilowatt", "KW")
    lazy val kW = kilowatt
    lazy val megawatt = unit("megawatt", "MW")
    lazy val MW = megawatt
    lazy val gigawatt = unit("gigawatt", "GW")
    lazy val GW = gigawatt
    lazy val milliwatt = unit("milliwatt", "mW")
    lazy val horsepower = unit("horsepower", "hp")
    lazy val lightBulb = unit("light bulb", "light bulb")
    lazy val hooverDam = unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
    lazy val mustangGT = unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

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