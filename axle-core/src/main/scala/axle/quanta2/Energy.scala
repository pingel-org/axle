package axle.quanta2

import axle.graph.DirectedGraph
import spire.math.Rational
import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class Energy extends Quantum {
  def wikipediaUrl = "TODO"
}

object Energy extends Energy {

  type Q = Energy
  
  def units[N: Field: Eq] = List.empty[UnitOfMeasurement[Q, N]]
  
  def links[N: Field: Eq] = List.empty[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]  
  
}
