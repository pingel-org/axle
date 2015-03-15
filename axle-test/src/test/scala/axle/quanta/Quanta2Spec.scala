package axle.quanta

import org.specs2.mutable._
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.moduleOps
import spire.math.Rational
import spire.algebra.Module
import spire.implicits._
import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung

class QuantaSpec extends Specification {

  "Scalar conversion" should {
    "work" in {

      implicit val dr = Distance.metadata[Rational, JungDirectedGraph]
      import dr._
      implicit val tr = Time.metadata[Rational, JungDirectedGraph]
      import tr._

      val d1 = Rational(3, 4) *: meter
      val d2 = Rational(7, 2) *: meter
      val t1 = Rational(4) *: second
      val t2 = Rational(9, 88) *: second
      val t3 = Rational(5d) *: second
      val t4 = 10 *: second

      val d3 = d1 + d2
      val d4 = d2 - d2

      //val d5 = d2 + t2 // shouldn't compile
      val t5 = t2 in minute
      val t6 = t1 :* Rational(5, 2)
      val t8 = Rational(5, 3) *: t1
      val t9 = t1 :* 60

      1 must be equalTo 1
    }
  }

  "Scalar conversion" should {
    "work" in {

      val md = Mass.metadata[Double, JungDirectedGraph]
      import md._

      (5 *: gram).magnitude must be equalTo 5

      implicit val dd = Distance.metadata[Double, JungDirectedGraph]
      import dd._
      import spire.implicits.DoubleAlgebra

      ((1d *: parsec) + (4d *: lightyear)).magnitude must be equalTo 7.260
      ((4d *: lightyear) + (1d *: parsec)).magnitude must be equalTo 2.226993865030675
    }
  }

  "Quanta conversion" should {

    "work" in {

      implicit val md = Mass.metadata[Double, JungDirectedGraph]
      import md._
      implicit val dd = Distance.metadata[Double, JungDirectedGraph]
      import dd._
      import spire.implicits.DoubleAlgebra

      ((1d *: kilogram) in gram).magnitude must be equalTo 1000d
      ((1d *: megagram) in milligram).magnitude must be equalTo 1000000000d
      ((1d *: mile) in ft).magnitude must be equalTo 5280d

    }

    "use Rational" in {

      implicit val vr = Volume.metadata[Rational, JungDirectedGraph]
      import vr._

      ((Rational(24) *: wineBottle) in nebuchadnezzar).magnitude must be equalTo Rational(6, 5)
    }
  }

  "addition" should {
    "work" in {

      implicit val md = Mass.metadata[Double, JungDirectedGraph]
      import md._
      implicit val dd = Distance.metadata[Double, JungDirectedGraph]
      import dd._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram

      // val mx = axle.quanta.modulize4[Double, Distance[Double], JungDirectedGraph] // fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]

      val module = implicitly[Module[UnittedQuantity[Distance, Double], Double]]
      val d1 = 1d *: meter
      val d2 = 1d *: foot
      module.plus(d1, d2)

      ((1d *: meter) + (1d *: foot)).magnitude must be equalTo 4.2808398950131235
      ((1d *: gram) + (1d *: kilogram)).magnitude must be equalTo 1.001
    }
  }

  "over" should {
    "work" in {

      val vr = Volume.metadata[Rational, JungDirectedGraph]
      import vr._
      val fr = Flow.metadata[Rational, JungDirectedGraph]
      import fr._

      // TODO convert that to years
      (1d *: greatLakes).over[Flow, Time, Rational](1d *: niagaraFalls).name must be equalTo "TODO" // TODO
    }
  }

}
