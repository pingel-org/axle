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

class Information extends Quantum {
  def wikipediaUrl = "TODO"
}

object Information extends Information {

  val second = newUnit[Information, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Information, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Information, Rational]

  val minute = Rational(60) *: second
  
}
