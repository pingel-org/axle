package axle

import scala.Stream.cons
import scala.Vector

import cats.Functor
import cats.kernel.Eq

import spire.algebra.Field
import spire.algebra.NRoot
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.math.log
import spire.math.ConvertableFrom
import spire.math.ConvertableTo
import spire.math.Rational
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.algebra.RegionEq
import axle.algebra.Aggregatable
import axle.algebra.tuple2Field
import axle.quanta.Information
import axle.quanta.InformationConverter
import axle.quanta.UnittedQuantity

package object stats {

  val rationalProbabilityDist: Dist[Rational] = {
    val denominator = Integer.MAX_VALUE - 1 // 1000000
    val x = (i: Int) => Rational(i.toLong, denominator.toLong)
    val y = Dist.intrange(0, denominator)
    Dist(x)(y)
  }

  def coinSides = Vector('HEAD, 'TAIL)

  def coin(pHead: Rational = Rational(1, 2)): ConditionalProbabilityTable[Symbol, Rational] =
    ConditionalProbabilityTable[Symbol, Rational](
      Map(
        'HEAD -> pHead,
        'TAIL -> (1 - pHead)))

  def bernoulliDistribution(pOne: Rational): ConditionalProbabilityTable[Int, Rational] = {
    import cats.implicits._
    ConditionalProbabilityTable[Int, Rational](
      Map(
        1 -> pOne,
        0 -> (1 - pOne)))
  }
    

  def binaryDecision(yes: Rational): ConditionalProbabilityTable[Boolean, Rational] = {
    import cats.implicits._
    ConditionalProbabilityTable(Map(true -> yes, false -> (1 - yes)))
  }

  def uniformDistribution[T: Eq](values: Seq[T]): ConditionalProbabilityTable[T, Rational] = {

    val grouped = values.groupBy(identity)
    val dist: Map[T, Rational] = grouped.map({ kvs =>
       val rk = kvs._1
       val v = Rational(kvs._2.size.toLong, values.size.toLong)
       rk -> v
    })

    ConditionalProbabilityTable(dist)
  }

  def iffy[T, N, C[_, _], M[_, _]](
    conditionModel:   C[Boolean, N],
    trueBranchModel:  M[T, N],
    falseBranchModel: M[T, N])(
    implicit
    eqT: Eq[T],
    fieldN: Field[N],
    eqN: cats.kernel.Eq[N],
    pIn:  ProbabilityModel[C],
    pOut: ProbabilityModel[M]): M[T, N] = {

    implicit val eqBool = cats.kernel.Eq.fromUniversalEquals[Boolean]
    val pTrue: N = pIn.probabilityOf(conditionModel)(RegionEq(true))
    val pFalse: N = pIn.probabilityOf(conditionModel)(RegionEq(false))

    pOut.mapValues(pOut.adjoin(trueBranchModel)(falseBranchModel))({ case (v1, v2) => (v1 * pTrue) + (v2 * pFalse) })
  }

  def log2[N: Field: ConvertableFrom](x: N): Double =
    log(ConvertableFrom[N].toDouble(x)) / log(2d)

  def square[N: Ring](x: N): N = x ** 2

  /**
   *
   * https://en.wikipedia.org/wiki/Root-mean-square_deviation
   */

  def rootMeanSquareDeviation[C[_], X](
    data:      C[X],
    estimator: X => X)(
    implicit
    functor: Functor[C],
    agg:     Aggregatable[C],
    field:   Field[X],
    nroot:   NRoot[X]): X = {
    import cats.syntax.all._
    nroot.sqrt(Σ[X, C](data.map(x => square(x - estimator(x)))))
  }

  def expectation[A: Eq: Field: ConvertableFrom, N: Field: ConvertableTo](model: ConditionalProbabilityTable[A, N]): N = {

    implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

    def a2n(a: A): N = ConvertableFrom[A].toType[N](a)(ConvertableTo[N])

    Σ[N, IndexedSeq](model.values.toVector.map { x =>
      prob.probabilityOf(model)(RegionEq(x)) * a2n(x)
    })
  }


  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def standardDeviation[A: Eq: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A = {

    implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

    def n2a(n: N): A = ConvertableFrom[N].toType[A](n)(ConvertableTo[A])

    val μ: A = Σ[A, IndexedSeq](model.values.toVector.map({ x => n2a(prob.probabilityOf(model)(RegionEq(x))) * x }))

    val sum: A = Σ[A, IndexedSeq](model.values.toVector map { x => n2a(prob.probabilityOf(model)(RegionEq(x))) * square(x - μ) })

    NRoot[A].sqrt(sum)
  }

  def σ[A: Eq: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A =
    standardDeviation[A, N](model)

  def stddev[ A: Eq: NRoot: Field: Manifest: ConvertableTo, N: Field: Manifest: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A =
    standardDeviation[A, N](model)

  def entropy[A: Eq: Manifest, N: Field: Eq: ConvertableFrom](model: ConditionalProbabilityTable[A, N])(
    implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] = {

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

    implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

    val convertN = ConvertableFrom[N]
    val H = Σ[Double, Iterable](model.values map { a: A =>
      val px: N = prob.probabilityOf(model)(RegionEq(a))
      import cats.syntax.all._
      if (px === Field[N].zero) {
        0d
      } else {
        -convertN.toDouble(px) * log2(px)
      }
    })
    UnittedQuantity(H, convert.bit)
  }

  def H[A: Eq: Manifest, N: Field: Eq: ConvertableFrom](model: ConditionalProbabilityTable[A, N])(
    implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] =
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


  // implicit def monadForProbabilityModel[M[_, _], V](
  //   implicit
  //    fieldV: Field[V],
  //    prob: ProbabilityModel[M]): Monad[({ type λ[T] = M[T, V] })#λ] =
  //   new Monad[({ type λ[T] = M[T, V] })#λ] {

  //     def pure[A](a: A): M[A, V] =
  //       prob.construct(Variable[A]("a"), Vector(a), (a: A) => fieldV.one)

  //     def tailRecM[A, B](a: A)(f: A => M[Either[A, B], V]): M[B, V] =
  //       ???

  //     override def map[A, B](model: M[A, V])(f: A => B): M[B, V] = {

  //       val b2n = prob
  //         .values(model)
  //         .map({ v => f(v) -> prob.probabilityOfExpression(model)(v) })
  //         .groupBy(_._1)
  //         .mapValues(_.map(_._2).reduce(fieldV.plus))

  //         prob.construct(Variable[B]("b"), b2n.keys, b2n)
  //     }

  //     override def flatMap[A, B](model: M[A, V])(f: A => M[B, V]): M[B, V] = {

  //       val foo = prob.values(model)
  //         .flatMap(a => {
  //           val p = prob.probabilityOfExpression(model)(a)
  //           val subDistribution = f(a)
  //           prob.values(subDistribution).map(b => {
  //             b -> (fieldV.times(p, prob.probabilityOf(subDistribution)(b)))
  //           })
  //         })

  //       val b2n =
  //         foo
  //           .groupBy(_._1)
  //           .mapValues(_.map(_._2).reduce(fieldV.plus))

  //           prob.construct(Variable[B]("b"), b2n.keys, b2n)
  //     }
  //   }

}
