package axle.quanta

import org.specs2.mutable._

import spire.math._

class QuantaSpec extends Specification {

  import axle._

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

      greatLakes.over(niagaraFalls, Time).magnitude must be equalTo 1.0 // TODO convert that to years
    }
  }

}
