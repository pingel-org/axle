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

class Speed extends Quantum {
  def wikipediaUrl = "TODO"
}

object Speed extends Speed {

  val second = newUnit[Speed, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Speed, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Speed, Rational]

  val minute = Rational(60) *: second
  
}
