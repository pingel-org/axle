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

class Mass extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"
}

object Mass extends Mass {

  val gram = newUnit[Mass, Rational]
  
  implicit val cgMR: DirectedGraph[Quantity[Mass, Rational], Rational => Rational] = ???

  implicit val mtm = modulize[Mass, Rational]

  val kilogram = Rational(1000) *: gram
  val megagram = Rational(1000) *: kilogram
  val milligram = Rational(1, 1000) *: gram
  
}
