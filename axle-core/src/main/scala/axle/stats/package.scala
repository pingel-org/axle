package axle

import scala.Stream.cons
import scala.Vector
import scala.language.implicitConversions
import scala.util.Random.nextDouble
import scala.util.Random.nextInt

import axle.algebra.Σ
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Aggregatable
import axle.quanta.Information
import axle.quanta.InformationConverter
import axle.quanta.UnittedQuantity
import axle.syntax.functor.functorOps
import spire.algebra.AdditiveMonoid
import cats.kernel.Eq
import cats.implicits._
import spire.algebra.Field
import spire.algebra.NRoot
import cats.kernel.Order
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.convertableOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo
import spire.math.Rational
import spire.random.Dist

package object stats {

  implicit val rationalProbabilityDist: Dist[Rational] = {
    val biggishInt = 1000000
    Dist(Rational(_: Int, biggishInt))(Dist.intrange(0, biggishInt))
  }

  //implicit def evalProbability[N]: Probability[N] => N = _()

  implicit def enrichCaseGenTraversable[A: Manifest, N: Field](cgt: Iterable[Case[A, N]]): EnrichedCaseGenTraversable[A, N] = EnrichedCaseGenTraversable(cgt)

  val sides = Vector('HEAD, 'TAIL)

  def coin(pHead: Rational = Rational(1, 2)): Distribution[Symbol, Rational] =
    ConditionalProbabilityTable0[Symbol, Rational](
      Map('HEAD -> pHead, 'TAIL -> (1 - pHead)), "coin")

  def binaryDecision(yes: Rational): Distribution0[Boolean, Rational] =
    ConditionalProbabilityTable0(Map(true -> yes, false -> (1 - yes)), s"binaryDecision $yes")

  def uniformDistribution[T](values: Seq[T], name: String): Distribution0[T, Rational] = {

    val dist = values.groupBy(identity).mapValues({ ks => Rational(ks.size.toLong, values.size.toLong) }).toMap

    ConditionalProbabilityTable0(dist, name)
  }

  def iffy[C: Eq, N: Field: Order: Dist](
    decision: Distribution0[Boolean, N],
    trueBranch: Distribution[C, N],
    falseBranch: Distribution[C, N]): Distribution0[C, N] = {

    val addN = implicitly[AdditiveMonoid[N]]
    val pTrue = decision.probabilityOf(true)
    val pFalse = decision.probabilityOf(false)

    val parts = (trueBranch.values.map(v => (v, trueBranch.probabilityOf(v) * pTrue)) ++
      falseBranch.values.map(v => (v, falseBranch.probabilityOf(v) * pFalse)))

    val newDist = parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(addN.plus)).toMap

    ConditionalProbabilityTable0(newDist, "todo")
  }

  def log2[N: Field: ConvertableFrom](x: N) =
    spire.math.log(x.toDouble) / spire.math.log(2d)

  def square[N: Ring](x: N): N = x ** 2

  /**
   *
   * https://en.wikipedia.org/wiki/Root-mean-square_deviation
   */

  def rootMeanSquareDeviation[C, X, D](
    data: C,
    estimator: X => X)(
      implicit finite: Finite[C, X],
      functor: Functor[C, X, X, D],
      agg: Aggregatable[D, X, X],
      field: Field[X],
      nroot: NRoot[X]): X =
    nroot.sqrt(Σ[X, D](data.map(x => square(x - estimator(x)))))

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def standardDeviation[A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    distribution: Distribution[A, N]): A = {

    def n2a(n: N): A = ConvertableFrom[N].toType[A](n)(ConvertableTo[A])

    val μ: A = Σ[A, IndexedSeq[A]](distribution.values.map({ x => n2a(distribution.probabilityOf(x)) * x }))

    Σ[A, IndexedSeq[A]](distribution.values map { x => n2a(distribution.probabilityOf(x)) * square(x - μ) }).sqrt
  }

  def σ[A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](distribution: Distribution[A, N]): A =
    standardDeviation(distribution)

  def stddev[A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](distribution: Distribution[A, N]): A =
    standardDeviation(distribution)

  def entropy[A: Manifest, N: Field: Eq: ConvertableFrom](
    X: Distribution[A, N])(
      implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] = {

    import spire.implicits.DoubleAlgebra

    val convertN = ConvertableFrom[N]
    val H = Σ[Double, IndexedSeq[Double]](X.values map { x =>
      val px: N = axle.stats.P(X is x).apply()
      if (px === Field[N].zero) {
        0d
      } else {
        convertN.toDouble(-px) * log2(px)
      }
    })
    UnittedQuantity(H, convert.bit)
  }

  def H[A: Manifest, N: Field: Eq: ConvertableFrom](
    X: Distribution[A, N])(implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] =
    entropy(X)

  def _reservoirSampleK[N](k: Int, i: Int, reservoir: List[N], xs: Stream[N]): Stream[List[N]] =
    if (xs.isEmpty) {
      cons(reservoir, Stream.empty)
    } else {
      val newReservoir =
        if (i < k) {
          xs.head :: reservoir
        } else {
          val r = nextDouble
          if (r < (k / i.toDouble)) {
            val skip = nextInt(reservoir.length)
            xs.head :: (reservoir.zipWithIndex.filterNot({ case (e, i) => i == skip }).map(_._1))
          } else {
            reservoir
          }
        }
      cons(newReservoir, _reservoirSampleK(k, i + 1, newReservoir, xs.tail))
    }

  def reservoirSampleK[N](k: Int, xs: Stream[N]) = _reservoirSampleK(k, 0, Nil, xs)

}
