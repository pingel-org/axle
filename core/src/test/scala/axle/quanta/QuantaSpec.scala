package axle.quanta

import org.specs2.mutable._

import java.math.BigDecimal

class QuantaSpec extends Specification {

  import axle._

  "Scalar conversion" should {
    "work" in {

      import Mass._
      import Distance._

      ("5" *: gram).magnitude must be equalTo new BigDecimal("5")
      ("1" *: parsec + "4" *: lightyear).magnitude must be equalTo new BigDecimal("2.228")
      ("4" *: lightyear + "1" *: parsec).magnitude must be equalTo new BigDecimal("7.26")

    }
  }

  "Quanta conversion" should {

    "work" in {

      import Distance._
      import Mass._

      (kilogram in gram).magnitude must be equalTo new BigDecimal("1E+3")
      (megagram in milligram).magnitude must be equalTo new BigDecimal("1.000000E+9")
      (mile in ft).magnitude must be equalTo new BigDecimal("5280")

    }
  }

  "addition" should {
    "work" in {

      import Mass._
      import Distance._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram
      // TODO (earth + sun).magnitude must be equalTo new BigDecimal("1988916.0936")
      // TODO (gram + kilogram).magnitude must be equalTo new BigDecimal("1001")
      1 should be equalTo(0)
    }
  }

  "over" should {
    "work" in {

      import Volume._
      import Flow._

      greatLakes.over(niagaraFalls, Time).magnitude must be equalTo new BigDecimal("12.36150")
      // TODO convert that to years
    }
  }

}
