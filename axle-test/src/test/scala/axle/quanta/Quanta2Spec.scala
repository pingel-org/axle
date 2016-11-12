package axle.quanta

import org.specs2.mutable._
import spire.math.Rational
import spire.algebra.Module
import spire.implicits._
import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.algebra.modules.rationalDoubleModule
import axle.algebra.modules.rationalRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import axle.orderToOrdering
import axle.spireToCatsEq
import cats.implicits._

class QuantaSpec extends Specification {

  "Scalar conversion" should {
    "work" in {

      implicit val dr = Distance.converterGraphK2[Rational, DirectedSparseGraph]
      import dr._
      implicit val tr = Time.converterGraphK2[Rational, DirectedSparseGraph]
      import tr._

      val d1 = Rational(3, 4) *: meter
      val d2 = Rational(7, 2) *: meter
      val t1 = Rational(4) *: second
      val t2 = Rational(9, 88) *: second
      val t3 = Rational(5d) *: second
      val t4 = 10 *: second

      val d3 = d1 + d2
      val d4 = d2 - d2

      val t5 = t2 in minute
      val t6 = t1 :* Rational(5, 2)
      val t8 = Rational(5, 3) *: (t1 in minute)
      val t9 = t1 :* 60

      t8.magnitude must be equalTo Rational(1, 9)
    }
  }

  "Scalar conversion" should {
    "work" in {

      val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
      import md._

      (5 *: gram).magnitude must be equalTo 5

      implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
      import dd._
      import spire.implicits.DoubleAlgebra

      ((1d *: parsec) + (4d *: lightyear)).magnitude must be equalTo 7.260
      ((4d *: lightyear) + (1d *: parsec)).magnitude must be equalTo 2.226993865030675
    }
  }

  "Quanta conversion" should {

    "work" in {

      implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
      import md._
      implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
      import dd._
      import spire.implicits.DoubleAlgebra

      ((1d *: kilogram) in gram).magnitude must be equalTo 1000d
      ((1d *: megagram) in milligram).magnitude must be equalTo 1000000000d
      ((1d *: mile) in ft).magnitude must be equalTo 5280d

    }

    "use Rational" in {

      implicit val vr = Volume.converterGraphK2[Rational, DirectedSparseGraph]
      import vr._

      ((Rational(1, 10) *: liter) in milliliter).magnitude must be equalTo Rational(100)
    }
  }

  "addition" should {
    "work" in {

      implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
      import md._
      implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
      import dd._

      // Shouldn't compile: gram + mile
      // Shouldn't compile: gram + kilogram + mile + gram

      // val mx = axle.quanta.modulize4[Double, Distance[Double], JungDirectedGraph] // fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]

      val module = Module[UnittedQuantity[Distance, Double], Double]
      val d1 = 1d *: meter
      val d2 = 1d *: foot
      module.plus(d1, d2)

      ((1d *: meter) + (1d *: foot)).magnitude must be equalTo 4.2808398950131235
      ((1d *: gram) + (1d *: kilogram)).magnitude must be equalTo 1.001
    }
  }

  "area" should {
    "order square meter and square centimeter" in {

      implicit val acg = Area.converterGraphK2[Double, DirectedSparseGraph]
      import acg._

      (1d *: m2) must be greaterThan (1d *: cm2)
    }
  }

  "acceleration" should {
    "order g and mpsps" in {

      implicit val acg = Acceleration.converterGraphK2[Double, DirectedSparseGraph]
      import acg._

      (1d *: g) must be greaterThan (1d *: mpsps)
    }
  }

  "force" should {
    "order newton and pound" in {

      implicit val fcg = Force.converterGraphK2[Double, DirectedSparseGraph]
      import fcg._

      (1d *: pound) must be greaterThan (1d *: newton)
    }
  }

  "frequency" should {
    "order KHz and Hz" in {

      implicit val fcg = Frequency.converterGraphK2[Double, DirectedSparseGraph]
      import fcg._

      (1d *: KHz) must be greaterThan (1d *: Hz)
    }
  }

  "energy" should {
    "order ton TNT and Joule" in {

      implicit val ecg = Energy.converterGraphK2[Double, DirectedSparseGraph]
      import ecg._

      (1d *: tonTNT) must be greaterThan (1d *: joule)
    }
  }

  "money" should {
    "define USD" in {

      implicit val mcg = Money.converterGraphK2[Double, DirectedSparseGraph]
      import mcg._

      (1d *: USD).magnitude must be equalTo 1d
    }
  }

  "money flow" should {
    "define USD per hour" in {

      implicit val mfcg = MoneyFlow.converterGraphK2[Double, DirectedSparseGraph]
      import mfcg._

      (1d *: USDperHour).magnitude must be equalTo 1d
    }
  }

  "money per force" should {
    "define USD per force" in {

      implicit val mfcg = MoneyPerForce.converterGraphK2[Double, DirectedSparseGraph]
      import mfcg._

      (1d *: USDperPound).magnitude must be equalTo 1d
    }
  }

  "power" should {
    "order watt and horsepower" in {

      implicit val pcg = Power.converterGraphK2[Double, DirectedSparseGraph]
      import pcg._

      (1d *: horsepower) must be greaterThan (1d *: watt)
    }
  }

  "speed" should {
    "order knot and mph" in {

      implicit val scg = Speed.converterGraphK2[Double, DirectedSparseGraph]
      import scg._

      (1d *: knot) must be greaterThan (1d *: mph)
    }
  }

  "temperature" should {
    "convert celsius, fahrenheit, and kelvin" in {

      implicit val tc = Temperature.converterGraphK2[Double, DirectedSparseGraph]
      import tc._

      ((0d *: celsius) in kelvin).magnitude must be equalTo 273.15d
      ((0d *: celsius) in fahrenheit).magnitude must be equalTo 32d
      ((212d *: fahrenheit) in celsius).magnitude must be equalTo 100d
    }
  }

  "over" should {
    "work" in {

      val vr = Volume.converterGraphK2[Rational, DirectedSparseGraph]
      import vr._
      val fr = Flow.converterGraphK2[Rational, DirectedSparseGraph]
      import fr._

      // TODO convert that to years
      (1d *: m3).over[Flow, Time, Rational](1d *: m3s).name must be equalTo "TODO" // TODO
    }
  }

}
