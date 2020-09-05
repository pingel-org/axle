package axle.laws

import org.scalacheck.Gen
import cats.kernel.Eq
import spire.math.Rational
import axle.algebra._

object TestSupport {

  val genPortion: Gen[Rational] =
    for {
      denominator <- Gen.oneOf(1 to 1000)
      numerator <- Gen.oneOf(0 to denominator)
    } yield Rational(numerator.toLong, denominator.toLong)

  def genRegionLeaf[T: Eq](xs: IndexedSeq[T]): Gen[Region[T]] =
    Gen.oneOf(
      Gen.oneOf(List(RegionEmpty[T]())),
      Gen.oneOf(List(RegionAll[T]())),
      Gen.oneOf(xs).map(RegionEq(_))
      // TODO RegionSet
      // TODO RegionIf
    )

  // TODO: With Order[T], include RegionGTE and RegionLTE

  def genRegion[T: Eq](xs: IndexedSeq[T]): Gen[Region[T]] =
    Gen.oneOf(
      genRegionLeaf(xs),
      genRegionLeaf(xs).map(RegionNegate(_)),
      genRegionLeaf(xs).flatMap(l => genRegionLeaf(xs).map(r => RegionAnd(l, r))),
      genRegionLeaf(xs).flatMap(l => genRegionLeaf(xs).map(r => RegionOr(l, r)))
    )

}
