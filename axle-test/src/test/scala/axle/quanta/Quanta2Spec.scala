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

      import DistanceRational._
      import Time._
      import TimeRational._

      val d1 = Rational(3, 4) *: meter
      val d2 = Rational(7, 2) *: meter
      val t1 = Rational(4) *: second
      val t2 = Rational(9, 88) *: second
      val t3 = Rational(5d) *: second
      val t4 = 10 *: second

      implicit val cgDistance = axle.quanta.conversionGraph[Distance[Rational], Rational, JungDirectedGraph](DistanceRational)

      val d3 = d1 + d2
      val d4 = d2 - d2

      implicit val cgTime = axle.quanta.conversionGraph[Time[Rational], Rational, JungDirectedGraph](TimeRational)

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

      import Mass._
      import MassDouble._
      import Distance._
      import DistanceDouble._
      import spire.implicits.DoubleAlgebra

      implicit val cgDistance = axle.quanta.conversionGraph[Distance[Double], Double, JungDirectedGraph](DistanceDouble)

      (5 *: gram).magnitude must be equalTo 5
      ((1 *: parsec) + (4 *: lightyear)).magnitude must be equalTo 7.260
      ((4 *: lightyear) + (1 *: parsec)).magnitude must be equalTo 2.226993865030675
    }
  }

  "Quanta conversion" should {

    "work" in {

      import Distance._
      import DistanceDouble._
      import Mass._
      import MassDouble._
      import spire.implicits.DoubleAlgebra

      implicit val cgDistance = axle.quanta.conversionGraph[Distance[Double], Double, JungDirectedGraph](DistanceDouble)

      implicit val cgMass = axle.quanta.conversionGraph[Mass[Double], Double, JungDirectedGraph](MassDouble)

      ((1 *: kilogram) in gram).magnitude must be equalTo 1000d
      ((1 *: megagram) in milligram).magnitude must be equalTo 1000000000d
      ((1 *: mile) in ft).magnitude must be equalTo 5280d

    }

    "use Rational" in {

      import Volume._
      import VolumeRational._

      implicit val cgVolume = axle.quanta.conversionGraph[Volume[Rational], Rational, JungDirectedGraph](VolumeRational)

      ((Rational(24) *: wineBottle) in nebuchadnezzar).magnitude must be equalTo Rational(6, 5)
    }
  }

  "addition" should {
    "work" in {

      import Mass._
      import MassDouble._
      import Distance._
      import DistanceDouble._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram

      implicit val cgDistance = axle.quanta.conversionGraph[Distance[Double], Double, JungDirectedGraph](DistanceDouble)

      implicit val cgMass = axle.quanta.conversionGraph[Mass[Double], Double, JungDirectedGraph](MassDouble)

      // val mx = axle.quanta.modulize4[Double, Distance[Double], JungDirectedGraph] // fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]

      val module = implicitly[Module[UnittedQuantity[Distance[Double], Double], Double]]
      val d1 = 1 *: meter
      val d2 = 1 *: foot
      module.plus(d1, d2)

      ((1 *: meter) + (1 *: foot)).magnitude must be equalTo 4.2808398950131235
      ((1 *: gram) + (1 *: kilogram)).magnitude must be equalTo 1.001
    }
  }

  "over" should {
    "work" in {

      import Volume._
      import VolumeRational._
      import Flow._
      import FlowRational._

      // TODO convert that to years
      (1 *: greatLakes).over[Flow[Rational], Time[Rational], Rational](1 *: niagaraFalls).magnitude must be equalTo Rational(1)
    }
  }

}
