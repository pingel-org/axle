package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

trait ForceUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Force](name, symbol, wiki)

  lazy val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  lazy val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  lazy val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

}

trait ForceMetadata[N] extends QuantumMetadata[Force, N] with ForceUnits

object Force {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Force, N, DG] with ForceMetadata[N] {

      def units: List[UnitOfMeasurement[Force]] =
        List(pound, newton, dyne)

      def links: Seq[(UnitOfMeasurement[Force], UnitOfMeasurement[Force], Bijection[N, N])] =
        List.empty

    }

}