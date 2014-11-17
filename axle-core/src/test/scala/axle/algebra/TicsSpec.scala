package axle.algebra

import scala.Vector

import org.specs2.mutable.Specification

import axle.quanta.Information
import axle.quanta.Information.bit
import axle.quanta.Information.cgn
import axle.quanta.UnittedQuantity
import axle.quanta.UnittedTics
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.implicits.SeqOrder
import spire.implicits._
import axle.quanta.Information._

class TicsSpec extends Specification {

  "Tics for UnittedQuantity" should {
    "work" in {

      implicit val dt = new DoubleTics {}

      val ticker = new UnittedTics[Information, Double](bit[Double])

      val tics = ticker.tics(0d *: bit[Double], 1d *: bit[Double]).toVector

      // TODO: configurable precision
      val expected = Vector(
        (0.0 *: bit[Double], "0.000000"),
        (0.1 *: bit[Double], "0.100000"),
        (0.2 *: bit[Double], "0.200000"),
        (0.3 *: bit[Double], "0.300000"),
        (0.4 *: bit[Double], "0.400000"),
        (0.5 *: bit[Double], "0.500000"),
        (0.6 *: bit[Double], "0.600000"),
        (0.7 *: bit[Double], "0.700000"),
        (0.8 *: bit[Double], "0.800000"),
        (0.9 *: bit[Double], "0.900000"),
        (1.0 *: bit[Double], "1.000000"))

      val vieq = implicitly[Eq[Vector[(UnittedQuantity[Information, Double], String)]]]

      println(s"actual  : $tics")
      println(s"expected: $expected")
      
      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

}