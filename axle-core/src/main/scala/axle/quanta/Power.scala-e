package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale10s
import axle.algebra.Scale
import cats.kernel.Eq
import spire.algebra.Field
import spire.algebra.Module

case class Power() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Power_(physics)"

}

trait PowerUnits extends QuantumUnits[Power] {

  lazy val watt = unit("watt", "W")
  lazy val kilowatt = unit("kilowatt", "KW")
  lazy val megawatt = unit("megawatt", "MW")
  lazy val gigawatt = unit("gigawatt", "GW")
  lazy val milliwatt = unit("milliwatt", "mW")
  lazy val horsepower = unit("horsepower", "hp") // Note: the Imperial version
  // TODO: foot-pound per second (fpps)

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

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit moduleDouble: Module[N, Double],
    evDG: DirectedGraph[DG[UnitOfMeasurement[Power], N => N], UnitOfMeasurement[Power], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Power], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit moduleDouble: Module[N, Double],
    evDG: DirectedGraph[DG, UnitOfMeasurement[Power], N => N]) =
    new UnitConverterGraph[Power, N, DG] with PowerConverter[N] {

      import spire.implicits.DoubleAlgebra

      def links: Seq[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])] =
        List[(UnitOfMeasurement[Power], UnitOfMeasurement[Power], Bijection[N, N])](
          (watt, kilowatt, Scale10s(3)),
          (kilowatt, megawatt, Scale10s(3)),
          (megawatt, gigawatt, Scale10s(3)),
          (watt, horsepower, Scale(745.7)),
          // TODO: horsepower = 550 fpps
          (milliwatt, watt, Scale10s(3)))

    }

}
