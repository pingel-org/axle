package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import axle.algebra.Scale10s
import spire.algebra.Eq
import spire.algebra.Field

case class Energy() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

}

trait EnergyUnits extends QuantumUnits[Energy] {

  lazy val kwh = unit("kwh", "kwh") // derive
  lazy val joule = unit("joule", "J")
  lazy val kilojoule = unit("kilojoule", "KJ")
  lazy val megajoule = unit("megajoule", "MJ")
  lazy val tonTNT = unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  lazy val t = tonTNT
  lazy val kiloton = unit("kiloton", "KT")
  lazy val kt = kiloton
  lazy val megaton = unit("megaton", "MT")
  lazy val mt = megaton
  lazy val gigaton = unit("gigaton", "GT")
  lazy val gt = gigaton

  def units: List[UnitOfMeasurement[Energy]] =
    List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

}

trait EnergyConverter[N] extends UnitConverter[Energy, N] with EnergyUnits {

  def defaultUnit = joule
}

object Energy {

  import spire.algebra.Module
  import spire.math._
  import spire.implicits._

  def converterGraph[N: Field: Eq, DG](
    implicit module: Module[N, Double],
    evDG: DirectedGraph[DG, UnitOfMeasurement[Energy], N => N]) =
    new UnitConverterGraph[Energy, N, DG] with EnergyConverter[N] {

      def links: Seq[(UnitOfMeasurement[Energy], UnitOfMeasurement[Energy], Bijection[N, N])] =
        List[(UnitOfMeasurement[Energy], UnitOfMeasurement[Energy], Bijection[N, N])](
          (megajoule, t, Scale(4.184)),
          (joule, kilojoule, Scale10s(3)),
          (joule, megajoule, Scale10s(6)),
          (t, kt, Scale10s(3)),
          (t, mt, Scale10s(6)),
          (t, gt, Scale10s(9)))

    }

}