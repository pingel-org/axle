package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Energy() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

}

trait EnergyUnits[N] {

  type U = UnitOfMeasurement[Energy, N]

  def kwh: U
  def joule: U
  def kilojoule: U
  def megajoule: U
  def tonTNT: U
  def t: U
  def kiloton: U
  def kt: U
  def megaton: U
  def mt: U
  def gigaton: U
  def gt: U
}

trait EnergyMetadata[N] extends QuantumMetadata[Energy, N] with EnergyUnits[N]

object Energy {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Energy, N, DG] with EnergyMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Energy, N](name, symbol, wiki)

      lazy val _kwh = unit("kwh", "kwh") // derive
      lazy val _joule = unit("joule", "J")
      lazy val _kilojoule = unit("kilojoule", "KJ")
      lazy val _megajoule = unit("megajoule", "MJ")
      lazy val _tonTNT = unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
      lazy val _kiloton = unit("kiloton", "KT")
      lazy val _megaton = unit("megaton", "MT")
      lazy val _gigaton = unit("gigaton", "GT")

      def kwh = _kwh
      def joule = _joule
      def kilojoule = _kilojoule
      def megajoule = _megajoule
      def tonTNT = _tonTNT
      def t = _tonTNT
      def kiloton = _kiloton
      def kt = _kiloton
      def megaton = _megaton
      def mt = _megaton
      def gigaton = _gigaton
      def gt = _gigaton

      // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

      def units: List[UnitOfMeasurement[Energy, N]] =
        List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

      def links: Seq[(UnitOfMeasurement[Energy, N], UnitOfMeasurement[Energy, N], Bijection[N, N])] =
        List[(UnitOfMeasurement[Energy, N], UnitOfMeasurement[Energy, N], Bijection[N, N])](
          (megajoule, t, ScaleDouble(4.184)),
          (joule, kilojoule, Scale10s(3)),
          (joule, megajoule, Scale10s(6)),
          (t, kt, Scale10s(3)),
          (t, mt, Scale10s(6)),
          (t, gt, Scale10s(9)))

    }

}