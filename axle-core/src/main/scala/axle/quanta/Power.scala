package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale10s
import spire.algebra.Eq
import spire.algebra.Field

case class Power() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

}

trait PowerUnits extends QuantumUnits[Power] {

  lazy val watt = unit("watt", "W")
  lazy val kilowatt = unit("kilowatt", "KW")
  lazy val megawatt = unit("megawatt", "MW")
  lazy val gigawatt = unit("gigawatt", "GW")
  lazy val milliwatt = unit("milliwatt", "mW")
  lazy val horsepower = unit("horsepower", "hp")

  lazy val W = watt
  lazy val kW = kilowatt
  lazy val MW = megawatt
  lazy val GW = gigawatt

  def units: List[UnitOfMeasurement[Power]] =
    List(watt, kilowatt, megawatt, gigawatt, milliwatt, horsepower)

}

trait PowerConverter[N] extends UnitConverter[Power, N] with PowerUnits {

  def defaultUnit = watt
}

object Power {

  def converterGraph[N: Field: Eq, DG](
    implicit evDG: DirectedGraph[DG, UnitOfMeasurement[Power], N => N]) =
    new UnitConverterGraph[Power, N, DG] with PowerConverter[N] {

      def links: Seq[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])] =
        List[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])](
          (watt, kilowatt, Scale10s(3)),
          (kilowatt, megawatt, Scale10s(3)),
          (megawatt, gigawatt, Scale10s(3)),
          (milliwatt, watt, Scale10s(3)))

    }

}