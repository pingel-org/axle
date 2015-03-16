package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

trait ForceUnits extends QuantumUnits[Force] {

  lazy val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  lazy val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  lazy val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

  def units: List[UnitOfMeasurement[Force]] =
    List(pound, newton, dyne)

}

trait ForceConverter[N] extends UnitConverter[Force, N] with ForceUnits

object Force {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Force, N, DG] with ForceConverter[N] {

      def links: Seq[(UnitOfMeasurement[Force], UnitOfMeasurement[Force], Bijection[N, N])] =
        List.empty

    }

}