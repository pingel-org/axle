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

class Time extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Time extends Time {

  val second = newUnit[Time, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Time, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Time, Rational]

  val minute = 60 *: second
  
}
