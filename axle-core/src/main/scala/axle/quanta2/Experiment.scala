package axle.quanta2

import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.moduleOps
import spire.math.Rational

import Distance._
import Time._

object Experiment {

  val d1 = Rational(3, 4) *: meter
  val d2 = Rational(7, 2) *: meter
  val t1 = Rational(4) *: second
  val t2 = Rational(9, 88) *: second
  val t3 = Rational(5d) *: second

  val d3 = d1 + d2
  val d4 = d2 - d2
  //val d5 = d2 + t2 // shouldn't compile
  val t4 = t2 in minute
  val t6 = t1 :* Rational(5, 2)
  val t8 = Rational(5, 3) *: t1
  val t9 = t1 :* 60

  // TODO: show other number types, N
  
}
