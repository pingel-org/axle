package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Area() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Area"

}

trait AreaUnits extends QuantumUnits[Area] {

  lazy val m2 = unit("m2", "m2") // derive
  lazy val km2 = unit("km2", "km2") // derive
  lazy val cm2 = unit("cm2", "cm2") // derive

  def units: List[UnitOfMeasurement[Area]] =
    List(m2, km2, cm2)

}

trait AreaMetadata[N] extends QuantumMetadata[Area, N] with AreaUnits

object Area {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Area, N, DG] with AreaMetadata[N] {

      def links: Seq[(UnitOfMeasurement[Area], UnitOfMeasurement[Area], Bijection[N, N])] =
        List[(UnitOfMeasurement[Area], UnitOfMeasurement[Area], Bijection[N, N])](
          (m2, km2, Scale10s(6)),
          (cm2, m2, Scale10s(6)))

    }

}