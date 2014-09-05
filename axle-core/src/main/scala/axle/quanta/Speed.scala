package axle.quanta

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

abstract class Speed extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"
}

object Speed extends Speed {
  
  type Q = Speed
  
  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]]()
  
  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]()  
  }

  def x[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "x")
  
}
