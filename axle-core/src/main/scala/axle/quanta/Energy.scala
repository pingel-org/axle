package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Energy() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

}

trait EnergyUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Energy](name, symbol, wiki)

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

  // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

}

trait EnergyMetadata[N] extends QuantumMetadata[Energy, N] with EnergyUnits

object Energy {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Energy, N, DG] with EnergyMetadata[N] {

      def units: List[UnitOfMeasurement[Energy]] =
        List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

      def links: Seq[(UnitOfMeasurement[Energy], UnitOfMeasurement[Energy], Bijection[N, N])] =
        List[(UnitOfMeasurement[Energy], UnitOfMeasurement[Energy], Bijection[N, N])](
          (megajoule, t, ScaleDouble(4.184)),
          (joule, kilojoule, Scale10s(3)),
          (joule, megajoule, Scale10s(6)),
          (t, kt, Scale10s(3)),
          (t, mt, Scale10s(6)),
          (t, gt, Scale10s(9)))

    }

}