package axle.algebra

import spire.algebra._
import spire.math._
import spire.implicits.additiveGroupOps

package object metricspaces {

  implicit def wrappedStringSpace[N](
    implicit
    iscSpace: MetricSpace[IndexedSeq[Char], N]): MetricSpace[String, N] =
      (s1: String, s2: String) => iscSpace.distance(s1, s2)

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] =
    (v: Rational, w: Rational) =>
      spire.math.abs(v.toDouble - w.toDouble)

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] =
    (v: Real, w: Real) =>
      spire.math.abs(v.toDouble - w.toDouble)

  // implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] =
  //   (v: Double, w: Double) =>
  //     (v - w).abs

  implicit def metricSpaceFromAdditiveGroupSigned[N: AdditiveGroup: Signed]: MetricSpace[N, N] =
    (v: N, w: N) =>
      spire.math.abs(v - w)

}
