package axle.stats

import org.scalacheck.Gen
import spire.math.Rational
import axle.algebra.Region
import axle.algebra.RegionEq

object TestSupport {

  val genPortion: Gen[Rational] =
    for {
      denominator <- Gen.oneOf(1 to 1000)
      numerator <- Gen.oneOf(0 to denominator)
    } yield Rational(numerator.toLong, denominator.toLong)

  def genRegion[T: cats.kernel.Eq](xs: IndexedSeq[T]): Gen[Region[T]] = 
    Gen.oneOf(xs.map(RegionEq(_)))

}
