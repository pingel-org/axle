package axle.algebra

import scala.Vector

import org.specs2.mutable.Specification

import axle.quanta.Information3
import axle.quanta.Information3.bit
import axle.quanta.Information3.conversionGraph
import axle.quanta.UnittedQuantity
import axle.jung.JungDirectedGraph
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.implicits.SeqOrder
import spire.implicits._

class UnittedTicsSpec extends Specification {

  "Tics for UnittedQuantity" should {
    "work" in {

      implicit val base = bit[Double]
      implicit val cg = axle.quanta.Information3.conversionGraph[Double, JungDirectedGraph]
      val ticker = axle.quanta.unitted3Tics[Information3, Double, JungDirectedGraph]

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

      val vieq = implicitly[Eq[Vector[(UnittedQuantity[Information3, Double], String)]]]

      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

}