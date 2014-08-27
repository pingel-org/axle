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

class Volume extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Volume extends Volume {

  val second = newUnit[Volume, Rational]
  
  implicit val cgTR: DirectedGraph[Quantity[Volume, Rational], Rational => Rational] = ???

  implicit val mtr = modulize[Volume, Rational]

  val minute = Rational(60) *: second

  val greatLakes = Rational(1) *: second
  val megagram = Rational(1) *: second
  val wineBottle = Rational(1) *: second
  val nebuchadnezzar = Rational(5) *: wineBottle
  
}
