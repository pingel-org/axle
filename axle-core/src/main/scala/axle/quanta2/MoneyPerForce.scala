package axle.quanta2

import axle.graph.DirectedGraph
import spire.algebra._
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class MoneyPerForce extends Quantum {
  def wikipediaUrl = "TODO"
}

object MoneyPerForce extends MoneyPerForce {

  type Q = MoneyPerForce
  
  def units[N: Field: Eq] = List.empty[UnitOfMeasurement[Q, N]]
  
  def links[N: Field: Eq] = List.empty[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]  
  
}
