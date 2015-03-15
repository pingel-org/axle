package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

abstract class ForceMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadataGraph[Force, N, DG] {

  type U = UnitOfMeasurement[Force, N]

  def pound: U
  def newton: U
  def dyne: U
}

object Force {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new ForceMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Force, N](name, symbol, wiki)

    lazy val _pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
    lazy val _newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
    lazy val _dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

    def pound = _pound
    def newton = _newton
    def dyne = _dyne

    def units: List[UnitOfMeasurement[Force, N]] =
      List(pound, newton, dyne)

    def links: Seq[(UnitOfMeasurement[Force, N], UnitOfMeasurement[Force, N], Bijection[N, N])] =
      List.empty

  }

}