package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Power() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

}

trait PowerUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Power](name, symbol, wiki)

  lazy val watt = unit("watt", "W")
  lazy val kilowatt = unit("kilowatt", "KW")
  lazy val megawatt = unit("megawatt", "MW")
  lazy val gigawatt = unit("gigawatt", "GW")
  lazy val milliwatt = unit("milliwatt", "mW")
  lazy val horsepower = unit("horsepower", "hp")
  lazy val lightBulb = unit("light bulb", "light bulb")
  lazy val hooverDam = unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
  lazy val mustangGT = unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

  lazy val W = watt
  lazy val kW = kilowatt
  lazy val MW = megawatt
  lazy val GW = gigawatt
}

trait PowerMetadata[N] extends QuantumMetadata[Power, N] with PowerUnits

object Power {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Power, N, DG] with PowerMetadata[N] {

      def units: List[UnitOfMeasurement[Power]] =
        List(watt, kilowatt, megawatt, gigawatt, milliwatt, horsepower, lightBulb, hooverDam, mustangGT)

      def links: Seq[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])] =
        List[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])](
          (watt, kilowatt, Scale10s(3)),
          (kilowatt, megawatt, Scale10s(3)),
          (megawatt, gigawatt, Scale10s(3)),
          (milliwatt, watt, Scale10s(3)),
          (watt, lightBulb, ScaleInt(60)),
          (megawatt, hooverDam, ScaleInt(2080)),
          (horsepower, mustangGT, ScaleInt(420)))

    }

}