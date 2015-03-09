package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

trait ForceMetadata[N] extends QuantumMetadata[Force, N] {

  type U = UnitOfMeasurement[Force, N]

}

object Force {

  def metadata[N] = new ForceMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Force, N](name, symbol, wiki)

    lazy val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
    lazy val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
    lazy val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

    def units: List[UnitOfMeasurement[Force, N]] =
      List(pound, newton, dyne)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Force, N], UnitOfMeasurement[Force, N], Bijection[N, N])] =
      List.empty

  }

}