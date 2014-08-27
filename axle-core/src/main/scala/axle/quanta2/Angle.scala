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

class Angle extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"
}

object Angle extends Angle {

  val second = newUnit[Angle, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Angle, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Angle, Rational]

  val minute = Rational(60) *: second
  
}
