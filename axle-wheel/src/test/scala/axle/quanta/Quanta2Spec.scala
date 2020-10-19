package axle.quanta

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.math.Rational
import spire.math.Real
import spire.algebra._
import spire.implicits.additiveSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.rightModuleOps
import spire.implicits.leftModuleOps

import axle.algebra._
import axle.algebra.modules.doubleRationalModule
import axle.algebra.modules.rationalRationalModule
import axle.jung.directedGraphJung
import axle.laws.generator._

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.scalatest.Discipline

object ArbitraryUnittedQuantityStuff {

  implicit def arbUnit[Q, N](implicit uq: UnitConverter[Q, N]): Arbitrary[UnitOfMeasurement[Q]] =
    Arbitrary(genUnit)

  def arbitraryUQ[Q, N](
    implicit
    gq: Gen[UnittedQuantity[Q, N]]): Arbitrary[UnittedQuantity[Q, N]] =
    Arbitrary(gq)
}

class QuantaSpec extends AnyFunSuite with Matchers with Discipline {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
  import spire.laws._

  {
    import axle.algebra.modules.realRationalModule
    implicit val dd = Distance.converterGraphK2[Real, DirectedSparseGraph]
    val mudr = CModule[UnittedQuantity[Distance, Real], Real]

    import ArbitraryUnittedQuantityStuff._

    implicit val gr = gen.real

    val equq = UnittedQuantity.eqqqn[Distance, Real]

    val uqDistanceModuleLaws = VectorSpaceLaws[UnittedQuantity[Distance, Real], Real](
      equq,
      arbitraryUQ[Distance, Real](genUnittedQuantity[Distance, Real]),
      implicitly[Eq[Real]],
      arb.real,
      new org.typelevel.discipline.Predicate[Real] { def apply(a: Real) = true }).cModule(mudr)

    val agudr: cats.kernel.Group[UnittedQuantity[Distance, Real]] =
      axle.quanta.quantumAdditiveGroup[Distance, Real](
        spire.algebra.MultiplicativeMonoid[Real],
        dd,
        spire.algebra.AdditiveGroup[Real](algebra.ring.AdditiveGroup[Real](realRationalModule)))

    val uqDistanceAdditiveGroupLaws =
      GroupLaws[UnittedQuantity[Distance, Real]](
        equq,
        arbitraryUQ[Distance, Real](genUnittedQuantity[Distance, Real])).monoid(agudr)

    checkAll("Module Laws for Module[UnittedQuantity[Distance, Real]]", uqDistanceModuleLaws)

    checkAll("Additive Group Laws for AdditiveGroup[UnittedQuantity[Distance, Real]]", uqDistanceAdditiveGroupLaws)
  }

  test("Distance and Time scalar conversion") {

    implicit val dr = Distance.converterGraphK2[Rational, DirectedSparseGraph]
    import dr._
    implicit val tr = Time.converterGraphK2[Rational, DirectedSparseGraph]
    import tr._

    val d1 = Rational(3, 4) *: meter
    val d2 = Rational(7, 2) *: meter
    val t1 = Rational(4) *: second
    val t2 = Rational(9, 88) *: second
    Rational(5d) *: second
    10 *: second

    d1 + d2
    d2 - d2

    t2 in minute
    t1 :* Rational(5, 2)
    val t8 = Rational(5, 3) *: (t1 in minute)
    t1 :* 60

    t8.magnitude should be(Rational(1, 9))
  }

  test("Mass and Distance scalar conversion") {

    val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
    import md._

    (5 *: gram).magnitude should be(5)

    implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
    import dd._

    ((1d *: parsec) + (4d *: lightyear)).magnitude should be(7.260)
    ((4d *: lightyear) + (1d *: parsec)).magnitude should be(2.226993865030675)
  }

  test("Quanta conversion") {

    implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
    import md._
    implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
    import dd._

    ((1d *: kilogram) in gram).magnitude should be(1000d)
    ((1d *: megagram) in milligram).magnitude should be(1000000000d)
    ((1d *: mile) in ft).magnitude should be(5280d)

  }

  test("Quanta conversion with Rational") {

    implicit val vr = Volume.converterGraphK2[Rational, DirectedSparseGraph]
    import vr._

    ((Rational(1, 10) *: liter) in milliliter).magnitude should be(Rational(100))
  }

  test("addition") {

    implicit val md = Mass.converterGraphK2[Double, DirectedSparseGraph]
    import md._
    implicit val dd = Distance.converterGraphK2[Double, DirectedSparseGraph]
    import dd._

    // Shouldn't compile: gram + mile
    // Shouldn't compile: gram + kilogram + mile + gram

    // val mx = axle.quanta.modulize4[Double, Distance[Double], JungDirectedGraph] // fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]

    val module = CModule[UnittedQuantity[Distance, Double], Double]
    val d1 = 1d *: meter
    val d2 = 1d *: foot
    module.plus(d1, d2)

    ((1d *: meter) + (1d *: foot)).magnitude should be(4.2808398950131235)
    ((1d *: gram) + (1d *: kilogram)).magnitude should be(1.001)
  }

  test("order square meter and square centimeter") {

    implicit val acg = Area.converterGraphK2[Double, DirectedSparseGraph]
    import acg._

    (1d *: m2) should be > (1d *: cm2)
  }

  test("order g and mpsps") {

    implicit val acg = Acceleration.converterGraphK2[Double, DirectedSparseGraph]
    import acg._

    (1d *: g) should be > (1d *: mpsps)
  }

  test("order newton and pound") {

    implicit val fcg = Force.converterGraphK2[Double, DirectedSparseGraph]
    import fcg._

    (1d *: pound) should be > (1d *: newton)
  }

  test("order KHz and Hz") {

    implicit val fcg = Frequency.converterGraphK2[Double, DirectedSparseGraph]
    import fcg._

    (1d *: KHz) should be > (1d *: Hz)
  }

  test("order ton TNT and Joule") {

    implicit val ecg = Energy.converterGraphK2[Double, DirectedSparseGraph]
    import ecg._

    (1d *: tonTNT) should be > (1d *: joule)
  }

  test("define USD") {

    implicit val mcg = Money.converterGraphK2[Double, DirectedSparseGraph]
    import mcg._

    (1d *: USD).magnitude should be(1d)
  }

  test("define USD per hour") {

    implicit val mfcg = MoneyFlow.converterGraphK2[Double, DirectedSparseGraph]
    import mfcg._

    (1d *: USDperHour).magnitude should be(1d)
  }

  test("define USD per force") {

    implicit val mfcg = MoneyPerForce.converterGraphK2[Double, DirectedSparseGraph]
    import mfcg._

    (1d *: USDperPound).magnitude should be(1d)
  }

  test("order watt and horsepower") {

    implicit val pcg = Power.converterGraphK2[Double, DirectedSparseGraph]
    import pcg._

    (1d *: horsepower) should be > (1d *: watt)
  }

  test("order knot and mph") {

    implicit val scg = Speed.converterGraphK2[Double, DirectedSparseGraph]
    import scg._

    (1d *: knot) should be > (1d *: mph)
  }

  test("convert celsius, fahrenheit, and kelvin") {

    implicit val tc = Temperature.converterGraphK2[Double, DirectedSparseGraph]
    import tc._

    ((0d *: celsius) in kelvin).magnitude should be(273.15d)
    ((0d *: celsius) in fahrenheit).magnitude should be(32d)
    ((212d *: fahrenheit) in celsius).magnitude should be(100d)
  }

  test("over") {

    val vr = Volume.converterGraphK2[Rational, DirectedSparseGraph]
    import vr._
    val fr = Flow.converterGraphK2[Rational, DirectedSparseGraph]
    import fr._

    // TODO convert that to years
    (1d *: m3).over[Flow, Time, Rational](1d *: m3s).name should be("TODO") // TODO
  }

}
