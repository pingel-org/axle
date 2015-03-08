package axle.quanta

/**
 * Quantum
 *
 * Used in the sense of the World English Dictionary's 4th definition:
 *
 * 4. something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

import spire.algebra.Field
import spire.algebra.Eq
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Vertex

trait Quantum4[N] {

  type Q <: Quantum4[N]

  def wikipediaUrl: String

  def units: List[UnitOfMeasurement4[Q, N]]

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])]

}
