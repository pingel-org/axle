package axle.algebra

import org.scalacheck.Arbitrary
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.implicits.SeqNormedVectorSpace
import spire.math.Real
import spire.laws._

class MetricSpaceSpec() extends AnyFunSuite with Matchers with Discipline {

  import axle.laws.TestSupport._

  implicit val arbReal2: Arbitrary[(Real, Real)] = Arbitrary(genTuple2[Real, Real](gr, gr))

  def arbitraryRealIterableLengthN(n: Int): Arbitrary[Iterable[Real]] =
    Arbitrary(genRealIterableLengthN(n))
  
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

  implicit val r4 = arbitraryRealIterableLengthN(4)

  import axle.eqIterable

  implicit val asdf = new IterableRealSpace {}

  checkAll(
    "MetricSpace[Iterable[Real], Real]",
    VectorSpaceLaws[Iterable[Real], Real].metricSpace)

}
