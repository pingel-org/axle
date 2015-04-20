package axle.algebra

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import scala.collection.JavaConverters.asScalaBufferConverter

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.laws.MetricSpaceLaws
import spire.algebra.AdditiveMonoid
import spire.algebra.MetricSpace
import spire.implicits.SeqNormedVectorSpace
import spire.math.Real

trait ScalarDoubleSpace extends MetricSpace[Double, Double] {

  def distance(v: Double, w: Double): Double = math.abs(v - w)
}

trait ScalarRealSpace extends MetricSpace[Real, Real] {

  def distance(v: Real, w: Real): Real = (v - w).abs
}

trait RealTuple2Space extends MetricSpace[(Real, Real), Real] {

  def distance(v: (Real, Real), w: (Real, Real)): Real =
    ((v._1 - w._1) ** 2 + (v._2 - w._2) ** 2).sqrt
}

trait SeqRealSpace extends MetricSpace[Seq[Real], Real] {

  def distance(v: Seq[Real], w: Seq[Real]): Real = {
    assert(v.length == w.length)
    val parts = v.zip(w).map({ case (x, y) => (x - y) * (x - y) })
    val s = parts.reduce(implicitly[AdditiveMonoid[Real]].plus _)
    s.sqrt
  }
}

object ArbitrarySpaceStuff {

  lazy val genReal: Gen[Real] = Gen.chooseNum(-1000d, 1000000d, -1d, 0d, 1d).map(d => Real(d))

  implicit val arbReal: Arbitrary[Real] = Arbitrary(genReal)

  lazy val genReal2: Gen[(Real, Real)] =
    for {
      lr <- genReal
      rr <- genReal
    } yield (lr, rr)

  implicit val arbReal2: Arbitrary[(Real, Real)] = Arbitrary(genReal2)

  def genRealSeqLengthN(n: Int): Gen[Seq[Real]] =
    Gen.sequence((1 to n).map(i => genReal)).map(_.asScala)

  def arbitraryRealSeqLengthN(n: Int): Arbitrary[Seq[Real]] =
    Arbitrary(genRealSeqLengthN(n))
}

class MetricSpaceSpec() extends Specification with Discipline {

  //  lazy val genMetricSpace: Gen[MetricSpace[A, B]] = Gen.oneOf(spaces)
  //
  //  implicit lazy val arbitraryMetricSpace: Arbitrary[MetricSpace[A, B]] =
  //    Arbitrary(genMetricSpace)

  import ArbitrarySpaceStuff._

  implicit val rrr = new RealTuple2Space {}

  checkAll("MetricSpace[(Real, Real), Real", MetricSpaceLaws[(Real, Real), Real].cauchySchwarz)

  implicit val rr = new ScalarRealSpace {}

  checkAll("MetricSpace[Real, Real]", MetricSpaceLaws[Real, Real].cauchySchwarz)

  implicit val r4 = arbitraryRealSeqLengthN(4)

  checkAll("MetricSpace[Seq[Real], Real]", MetricSpaceLaws[Seq[Real], Real].cauchySchwarz)

}
