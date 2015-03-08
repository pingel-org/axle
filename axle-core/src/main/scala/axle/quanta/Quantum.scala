package axle.quanta

/**
 * Quantum
 *
 * Used in the sense of the World English Dictionary's th definition:
 *
 * . something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

import spire.algebra.Field
import spire.algebra.Eq
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Vertex

trait Quantum {

  def wikipediaUrl: String

}

trait QuantumMetadata[Q, N] {

  def units: List[UnitOfMeasurement[Q, N]]

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]

}

