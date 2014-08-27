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

class Flow extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
}

object Flow extends Flow {

  val second = newUnit[Flow, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Flow, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Flow, Rational]

  val niagaraFalls = Rational(60) *: second
  
}
