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

class Money extends Quantum {
  def wikipediaUrl = "TODO"
}

object Money extends Money {

  val second = newUnit[Money, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Money, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Money, Rational]

  val minute = Rational(60) *: second
  
}
