package axle.quanta

import axle.algebra.Bijection
import spire.algebra.Field

trait QuantumMetadata[Q, N] {

  def units: List[UnitOfMeasurement[Q, N]]

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]

}
