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

      implicit val dr = Distance.metadata[Rational]
      import dr._
      implicit val tr = Time.metadata[Rational]
      import tr._

      val d1 = Rational(3, 4) *: meter
      val d2 = Rational(7, 2) *: meter
      val t1 = Rational(4) *: second
      val t2 = Rational(9, 88) *: second
      val t3 = Rational(5d) *: second
      val t4 = 10 *: second

      implicit val cgDistance = axle.quanta.conversionGraph[Distance, Rational, JungDirectedGraph]

      val d3 = d1 + d2
      val d4 = d2 - d2

      implicit val cgTime = axle.quanta.conversionGraph[Time, Rational, JungDirectedGraph]

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

      val md = Mass.metadata[Double]
      import md._

      (5 *: gram).magnitude must be equalTo 5

      implicit val dd = Distance.metadata[Double]
      import dd._
      import spire.implicits.DoubleAlgebra

      implicit val cgDistance = axle.quanta.conversionGraph[Distance, Double, JungDirectedGraph]

      ((1 *: parsec) + (4 *: lightyear)).magnitude must be equalTo 7.260
      ((4 *: lightyear) + (1 *: parsec)).magnitude must be equalTo 2.226993865030675
    }
  }

  "Quanta conversion" should {

    "work" in {

      implicit val md = Mass.metadata[Double]
      import md._
      implicit val dd = Distance.metadata[Double]
      import dd._
      import spire.implicits.DoubleAlgebra

      implicit val cgDistance = axle.quanta.conversionGraph[Distance, Double, JungDirectedGraph]

      implicit val cgMass = axle.quanta.conversionGraph[Mass, Double, JungDirectedGraph]

      ((1 *: kilogram) in gram).magnitude must be equalTo 1000d
      ((1 *: megagram) in milligram).magnitude must be equalTo 1000000000d
      ((1 *: mile) in ft).magnitude must be equalTo 5280d

    }

    "use Rational" in {

      implicit val vr = Volume.metadata[Rational]
      import vr._

      implicit val cgVolume = axle.quanta.conversionGraph[Volume, Rational, JungDirectedGraph]

      ((Rational(24) *: wineBottle) in nebuchadnezzar).magnitude must be equalTo Rational(6, 5)
    }
  }

  "addition" should {
    "work" in {

      implicit val md = Mass.metadata[Double]
      import md._
      implicit val dd = Distance.metadata[Double]
      import dd._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram

      implicit val cgDistance = axle.quanta.conversionGraph[Distance, Double, JungDirectedGraph]

      implicit val cgMass = axle.quanta.conversionGraph[Mass, Double, JungDirectedGraph]

      // val mx = axle.quanta.modulize4[Double, Distance[Double], JungDirectedGraph] // fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]

      val module = implicitly[Module[UnittedQuantity[Distance, Double], Double]]
      val d1 = 1 *: meter
      val d2 = 1 *: foot
      module.plus(d1, d2)

      ((1 *: meter) + (1 *: foot)).magnitude must be equalTo 4.2808398950131235
      ((1 *: gram) + (1 *: kilogram)).magnitude must be equalTo 1.001
    }
  }

  "over" should {
    "work" in {

      val vr = Volume.metadata[Rational]
      import vr._
      val fr = Flow.metadata[Rational]
      import fr._

      // TODO convert that to years
      (1 *: greatLakes).over[Flow, Time, Rational](1 *: niagaraFalls).magnitude must be equalTo Rational(1)
    }
  }

}
