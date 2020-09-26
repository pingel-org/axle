package axle.laws

import org.scalacheck.Gen

import scala.jdk.CollectionConverters._

import cats.kernel.Eq

import spire.math._

import axle.algebra._

package object generator {

  def genPortion(
    minDenominatorSteps: Int,
    maxDenominatorSteps: Int,
    minNumerator: Int => Int,
    maxNumerator: Int => Int): Gen[Rational] =
    for {
      denominator <- Gen.oneOf(minDenominatorSteps to maxDenominatorSteps)
      numerator <- Gen.oneOf(minNumerator(denominator) to maxNumerator(denominator))
    } yield Rational(numerator.toLong, denominator.toLong)


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

  val gr: Gen[Real] = spire.laws.gen.real

  def genTuple2[T0, T1](implicit gen0: Gen[T0], gen1: Gen[T1]): Gen[(T0, T1)] = 
    for {
      v0 <- gen0
      v1 <- gen1
    } yield (v0, v1)

  def genRealIterableLengthN(n: Int): Gen[Iterable[Real]] = {
    val rs: Vector[Gen[Real]] = (1 to n).toVector.map(i => spire.laws.gen.real)
    Gen.sequence(rs).map(_.iterator().asScala.toList)
  }

}
