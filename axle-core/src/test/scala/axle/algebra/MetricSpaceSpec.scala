package axle.algebra

import org.specs2.ScalaCheck
import org.specs2.mutable._
import scala.collection.JavaConverters._
import spire.math._
import spire.implicits._
import spire.algebra._
import spire.syntax._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class MetricSpaceSpec[A: Eq: Arbitrary, B: AdditiveMonoid: Order](
  name: String, spaces: Seq[MetricSpace[A, B]])
  extends Specification with ScalaCheck {

  lazy val genMetricSpace: Gen[MetricSpace[A, B]] = Gen.oneOf(spaces)

  implicit lazy val arbitraryMetricSpace: Arbitrary[MetricSpace[A, B]] =
    Arbitrary(genMetricSpace)

  s"$name obeys Cauchy-Schwartz (aka Triangle Inequality)" ! prop { (ms: MetricSpace[A, B], x: A, y: A, z: A) =>
    implicit val ims = ms
    (x distance z) <= (x distance y) + (y distance z)
  }

}

object ScalarDoubleSpace extends MetricSpace[Double, Double] {

  def distance(v: Double, w: Double): Double = math.abs(v - w)
}

object ScalarRealSpace extends MetricSpace[Real, Real] {

  def distance(v: Real, w: Real): Real = (v - w).abs
}

object RealTuple2Space extends MetricSpace[(Real, Real), Real] {

  def distance(v: (Real, Real), w: (Real, Real)): Real =
    ((v._1 - w._1) ** 2 + (v._2 - w._2) ** 2).sqrt
}

object ScalarSeqRealSpace extends MetricSpace[Seq[Real], Real] {

  def distance(v: Seq[Real], w: Seq[Real]): Real = {
    assert(v.length == w.length)
    val parts = v.zip(w).map({ case (x, y) => (x - y) * (x - y) })
    val s = parts.reduce(implicitly[AdditiveMonoid[Real]].plus _)
    s.sqrt
  }
}

object ArbitrarySpaceStuff {

  lazy val genReal: Gen[Real] = Gen.chooseNum(-1000d, 1000000d, -1d, 0d, 1d).map(d => Real(d))

  lazy val genReal2: Gen[(Real, Real)] =
    for {
      lr <- genReal
      rr <- genReal
    } yield (lr, rr)

  def genRealListLengthN(n: Int): Gen[Traversable[Real]] =
    Gen.sequence((1 to n).map(i => genReal)).map(_.asScala)

  implicit val arbReal: Arbitrary[Real] = Arbitrary(genReal)

  implicit val arbReal2: Arbitrary[(Real, Real)] = Arbitrary(genReal2)

}

import ArbitrarySpaceStuff._

class RealMetricSpaceSpec
  extends MetricSpaceSpec("Scalar Real distance", List(ScalarRealSpace))

class RealTuple2MetricSpaceSpec
  extends MetricSpaceSpec("(Real, Real) distance", List(RealTuple2Space))

//class RealListMetricSpaceSpec
//  extends MetricSpaceSpec("List[Real] distance", List(ScalarSeqRealSpace))
