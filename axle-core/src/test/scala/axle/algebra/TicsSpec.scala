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
        (0.0, "0.0"),
        (0.1, "0.1"),
        (0.2, "0.2"),
        (0.3, "0.3"),
        (0.4, "0.4"),
        (0.5, "0.5"),
        (0.6, "0.6"),
        (0.7, "0.7"),
        (0.8, "0.8"),
        (0.9, "0.9"),
        (1.0, "1.0"))

      val vieq = implicitly[Eq[Vector[(Double, String)]]]
      
      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

}