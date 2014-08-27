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

class Force extends Quantum {
  def wikipediaUrl = "TODO"
}

object Force extends Force {

  val second = newUnit[Force, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Force, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Force, Rational]

  val minute = Rational(60) *: second
  
}
