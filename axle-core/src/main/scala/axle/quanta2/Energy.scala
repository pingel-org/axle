package axle.quanta2

import axle.graph.DirectedGraph
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

class Energy extends Quantum {
  def wikipediaUrl = "TODO"
}

object Energy extends Energy {

  val second = newUnit[Energy, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Energy, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Energy, Rational]

  val minute = Rational(60) *: second
  
}
