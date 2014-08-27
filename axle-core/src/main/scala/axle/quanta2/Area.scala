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

class Area extends Quantum {
  def wikipediaUrl = "TODO"
}

object Area extends Area {

  val second = newUnit[Area, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Area, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Area, Rational]

  val minute = Rational(60) *: second
  
}
