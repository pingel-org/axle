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

      val expected = Vector(
        (0.0 *: bit[Double], "0.0"),
        (0.1 *: bit[Double], "0.1"),
        (0.2 *: bit[Double], "0.2"),
        (0.3 *: bit[Double], "0.3"),
        (0.4 *: bit[Double], "0.4"),
        (0.5 *: bit[Double], "0.5"),
        (0.6 *: bit[Double], "0.6"),
        (0.7 *: bit[Double], "0.7"),
        (0.8 *: bit[Double], "0.8"),
        (0.9 *: bit[Double], "0.9"),
        (1.0 *: bit[Double], "1.0"))

      val vieq = implicitly[Eq[Vector[(UnittedQuantity[Information, Double], String)]]]

      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

}