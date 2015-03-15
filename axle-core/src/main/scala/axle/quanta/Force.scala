package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

trait ForceUnits {

  type U = UnitOfMeasurement[Force]

  def pound: U
  def newton: U
  def dyne: U
}

trait ForceMetadata[N] extends QuantumMetadata[Force, N] with ForceUnits

object Force {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Force, N, DG] with ForceMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Force](name, symbol, wiki)

      lazy val _pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
      lazy val _newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
      lazy val _dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

      def pound = _pound
      def newton = _newton
      def dyne = _dyne

      def units: List[UnitOfMeasurement[Force]] =
        List(pound, newton, dyne)

      def links: Seq[(UnitOfMeasurement[Force], UnitOfMeasurement[Force], Bijection[N, N])] =
        List.empty

    }

}