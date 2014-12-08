package axle.algebra

import scala.Vector

import org.specs2.mutable.Specification

import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.implicits.SeqOrder
import spire.implicits._

class TicsSpec extends Specification {

  "Tics for Double" should {
    "work" in {

      val tics = implicitly[Tics[Double]].tics(0d, 1d).toVector

      // TODO: configurable precision
      val expected = Vector(
        (0.0, "0.000000"),
        (0.1, "0.100000"),
        (0.2, "0.200000"),
        (0.3, "0.300000"),
        (0.4, "0.400000"),
        (0.5, "0.500000"),
        (0.6, "0.600000"),
        (0.7, "0.700000"),
        (0.8, "0.800000"),
        (0.9, "0.900000"),
        (1.0, "1.000000"))

      val vieq = implicitly[Eq[Vector[(Double, String)]]]

      println(s"tics     = $tics")
      println(s"expected = $expected")
      
      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

}