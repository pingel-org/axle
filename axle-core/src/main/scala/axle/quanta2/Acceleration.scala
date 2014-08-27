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

class Acceleration extends Quantum {
  def wikipediaUrl = "TODO"
}

object Acceleration extends Acceleration {

  val second = newUnit[Acceleration, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Acceleration, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Acceleration, Rational]

  val minute = Rational(60) *: second
  
}
