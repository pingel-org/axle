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

class MoneyPerForce extends Quantum {
  def wikipediaUrl = "TODO"
}

object MoneyPerForce extends MoneyPerForce {

  val second = newUnit[MoneyPerForce, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[MoneyPerForce, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[MoneyPerForce, Rational]

  val minute = Rational(60) *: second
  
}
