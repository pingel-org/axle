package axle.algebra

import scala.jdk.CollectionConverters
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.algebra.AdditiveMonoid
import spire.algebra.MetricSpace
import spire.implicits.SeqNormedVectorSpace
import spire.math.Real
import spire.laws._

import axle._

trait ScalarDoubleSpace extends MetricSpace[Double, Double] {

  def distance(v: Double, w: Double): Double = spire.math.abs(v - w)
}

trait ScalarRealSpace extends MetricSpace[Real, Real] {

  def distance(v: Real, w: Real): Real = (v - w).abs()
}

trait RealTuple2Space extends MetricSpace[(Real, Real), Real] {

  def distance(v: (Real, Real), w: (Real, Real)): Real =
    ((v._1 - w._1) ** 2 + (v._2 - w._2) ** 2).sqrt()
}

trait SeqRealSpace extends MetricSpace[Seq[Real], Real] {

  def distance(v: Seq[Real], w: Seq[Real]): Real = {
    assert(v.length == w.length)
    val parts = v.zip(w).map({ case (x, y) => (x - y) * (x - y) })
    val s = parts.reduce(implicitly[AdditiveMonoid[Real]].plus _)
    s.sqrt()
  }
}

object ArbitrarySpaceStuff {

  implicit val gr: Gen[Real] = gen.real

  implicit def genTuple2[T0, T1](implicit gen0: Gen[T0], gen1: Gen[T1]): Gen[(T0, T1)] = 
    for {
      v0 <- gen0
      v1 <- gen1
    } yield (v0, v1)

  implicit val arbReal2: Arbitrary[(Real, Real)] = Arbitrary(genTuple2[Real, Real])

  def genRealSeqLengthN(n: Int): Gen[Seq[Real]] =
    Gen.sequence((1 to n).map(i => gen.real)).map(_.asScala)

  def arbitraryRealSeqLengthN(n: Int): Arbitrary[Seq[Real]] =
    Arbitrary(genRealSeqLengthN(n))
}

class MetricSpaceSpec() extends AnyFunSuite with Matchers with Discipline {

  import ArbitrarySpaceStuff._

  import spire.laws.arb.real

  implicit val pred: Predicate[Real] = Predicate.const[Real](true)

  implicit val rrr = new RealTuple2Space {}

  checkAll(
    "MetricSpace[(Real, Real), Real]",
    VectorSpaceLaws[(Real, Real), Real].metricSpace)

  implicit val rr = new ScalarRealSpace {}

  checkAll(
    "MetricSpace[Real, Real]",
    VectorSpaceLaws[Real, Real].metricSpace)

  implicit val r4 = arbitraryRealSeqLengthN(4)

  checkAll(
    "MetricSpace[Seq[Real], Real]",
    VectorSpaceLaws[Seq[Real], Real].metricSpace)

}
