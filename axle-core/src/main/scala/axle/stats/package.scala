package axle

import scala.Stream.cons
import scala.Vector
import scala.language.implicitConversions

import cats.kernel.Eq
import cats.implicits._
import spire.algebra.Field

import spire.algebra.NRoot
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.math.log
import spire.math.ConvertableFrom
import spire.math.ConvertableTo
import spire.math.Rational
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.algebra.Functor
import axle.algebra.Aggregatable
import axle.quanta.Information
import axle.quanta.InformationConverter
import axle.quanta.UnittedQuantity
import axle.syntax.functor.functorOps

package object stats {

  implicit val rationalProbabilityDist: Dist[Rational] = {
    val denominator = Integer.MAX_VALUE - 1 // 1000000
    val x = (i: Int) => Rational(i.toLong, denominator.toLong)
    val y = Dist.intrange(0, denominator)
    Dist(x)(y)
  }

  implicit def enrichCaseGenTraversable[R, A: Manifest, N: Field](cgt: Iterable[CaseIs[A]]): EnrichedCaseGenTraversable[R, A, N] =
    EnrichedCaseGenTraversable(cgt)

  val sides = Vector('HEAD, 'TAIL)

  def coin(pHead: Rational = Rational(1, 2)): ConditionalProbabilityTable0[Symbol, Rational] =
    ConditionalProbabilityTable0[Symbol, Rational](
      Map('HEAD -> pHead,
          'TAIL -> (1 - pHead)),

    Variable(s"coin $pHead"))

  def binaryDecision(yes: Rational): ConditionalProbabilityTable0[Boolean, Rational] =
    ConditionalProbabilityTable0(Map(true -> yes, false -> (1 - yes)), Variable("binary"))

  def uniformDistribution[T](values: Seq[T], variable: Variable[T]): ConditionalProbabilityTable0[T, Rational] = {

    val dist = values.groupBy(identity).mapValues({ ks => Rational(ks.size.toLong, values.size.toLong) }).toMap

    ConditionalProbabilityTable0(dist, variable)
  }

  def iffy[T, N, C[_], M[_]](
    conditionModel: C[Boolean],
    trueBranchModel: M[T],
    falseBranchModel: M[T])(
      implicit pIn: ProbabilityModel[C, N],
      pOut: ProbabilityModel[M, N]): M[T] = {

    val pTrue: N = pIn.probabilityOf(conditionModel, true)
    val pFalse: N = pIn.probabilityOf(conditionModel, false)

    pOut.combine(Map(
        trueBranchModel -> pTrue,
        falseBranchModel -> pFalse))
  }

  def log2[N: Field: ConvertableFrom](x: N): Double =
    log(ConvertableFrom[N].toDouble(x)) / log(2d)

  def square[N: Ring](x: N): N = x ** 2

  /**
   *
   * https://en.wikipedia.org/wiki/Root-mean-square_deviation
   */

  def rootMeanSquareDeviation[C, X, D](
    data: C,
    estimator: X => X)(
      implicit functor: Functor[C, X, X, D],
      agg: Aggregatable[D, X, X],
      field: Field[X],
      nroot: NRoot[X]): X =
    nroot.sqrt(Σ[X, D](data.map(x => square(x - estimator(x)))))

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def standardDeviation[M[_], A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: M[A])(implicit prob: ProbabilityModel[M, N]): A = {

    def n2a(n: N): A = ConvertableFrom[N].toType[A](n)(ConvertableTo[A])

    val μ: A = Σ[A, IndexedSeq[A]](prob.values(model).map({ x => n2a(prob.probabilityOf(model, x)) * x }))

    val sum: A = Σ[A, IndexedSeq[A]](prob.values(model) map { x => n2a(prob.probabilityOf(model, x)) * square(x - μ) })

    NRoot[A].sqrt(sum)
  }

  def σ[M[_], A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: M[A])(implicit prob: ProbabilityModel[M, N]): A =
    standardDeviation[M, A, N](model)

  def stddev[M[_], A: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: M[A])(implicit prob: ProbabilityModel[M, N]): A =
    standardDeviation[M, A, N](model)

  def entropy[M[_], A: Manifest, N: Field: Eq: ConvertableFrom](model: M[A])(
      implicit prob: ProbabilityModel[M, N],
      convert: InformationConverter[Double]): UnittedQuantity[Information, Double] = {

    import spire.implicits.DoubleAlgebra

    val convertN = ConvertableFrom[N]
    val H = Σ[Double, IndexedSeq[Double]](prob.values(model) map { x =>
      val px: N = P(model, x)(prob).apply()
      if (px === Field[N].zero) {
        0d
      } else {
        -convertN.toDouble(px) * log2(px)
      }
    })
    UnittedQuantity(H, convert.bit)
  }

  def H[M[_], A: Manifest, N: Field: Eq: ConvertableFrom](model: M[A])(
      implicit prob: ProbabilityModel[M, N],
      convert: InformationConverter[Double]): UnittedQuantity[Information, Double] =
    entropy(model)

  def _reservoirSampleK[N](k: Int, i: Int, reservoir: List[N], xs: Stream[N], gen: Generator): Stream[List[N]] =
    if (xs.isEmpty) {
      cons(reservoir, Stream.empty)
    } else {
      val newReservoir =
        if (i < k) {
          xs.head :: reservoir
        } else {
          val r = gen.nextDouble()
          if (r < (k / i.toDouble)) {
            val skip = gen.nextInt(reservoir.length)
            xs.head :: (reservoir.zipWithIndex.filterNot({ case (e, i) => i == skip }).map(_._1))
          } else {
            reservoir
          }
        }
      cons(newReservoir, _reservoirSampleK(k, i + 1, newReservoir, xs.tail, gen))
    }

  def reservoirSampleK[N](k: Int, xs: Stream[N], gen: Generator) =
    _reservoirSampleK(k, 0, Nil, xs, gen)

}
