package axle.quanta2

import org.specs2.mutable._
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.moduleOps
import spire.math.Rational

import Distance._
import Time._

class Quanta2Spec extends Specification {

  "Scalar conversion" should {
    "work" in {

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

      1 must be equalTo 1
    }
  }

  "Scalar conversion" should {
    "work" in {

      import Mass._
      import Distance._

      (5 *: gram).magnitude must be equalTo 5
      (1 *: parsec + 4 *: lightyear).magnitude must be equalTo 7.260
      (4 *: lightyear + 1 *: parsec).magnitude must be equalTo 2.226993865030675 // TODO what precision do I want here?
    }
  }

  "Quanta conversion" should {

    "work" in {

      import Distance._
      import Mass._

      (kilogram in gram).magnitude.doubleValue must be equalTo 1000.0 // TODO precision
      (megagram in milligram).magnitude.doubleValue must be equalTo 1000000000.0 // TODO precision
      (mile in ft).magnitude.doubleValue must be equalTo 5280.0 // TODO precision

    }

    "use Rational" in {
      import Volume._
      ((24 *: wineBottle) in nebuchadnezzar).magnitude must be equalTo Rational(6, 5)
    }
  }

  "addition" should {
    "work" in {

      import Mass._
      import Distance._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram
      (meter + foot).magnitude must be equalTo 4.2808398950131235 // TODO what precision do I want here?
      (gram + kilogram).magnitude must be equalTo 1.001
    }
  }

  "over" should {
    "work" in {

      import Volume._
      import Flow._

      greatLakes.over[Flow, Time, Rational](niagaraFalls).magnitude must be equalTo Rational(1) // TODO convert that to years
    }
  }

}
