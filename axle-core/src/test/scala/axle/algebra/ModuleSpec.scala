package axle.algebra

import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import spire.math._
import spire.laws._
import spire.laws.arb._

import axle.algebra.modules._

class ModuleSpec() extends FunSuite with Matchers with Discipline {

  implicit val predReal = Predicate.const[Real](true)
  implicit val predRational = Predicate.const[Rational](true)

  checkAll("Module[Real, Rational]", VectorSpaceLaws[Real, Rational].module)
  checkAll("Module[Rational, Rational]", VectorSpaceLaws[Rational, Rational].module)
  checkAll("Module[Real, Real]", VectorSpaceLaws[Real, Real].module)

  // Lawless "dogs"
  //
  // Module[Double, Int]
  // Module[Double, Double]
  // Module[Real, Double]
  // Module[Double, Rational]
  // Module[Float, Rational]
  // Module[Float, Double]
  // Module[Rational, Double]

}
