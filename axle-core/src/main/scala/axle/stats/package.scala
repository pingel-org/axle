package axle

import cats.Functor
import cats.kernel.Eq

import spire.algebra.Field
import spire.algebra.NRoot
import spire.implicits.additiveGroupOps
//import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo
//import spire.math.Rational
import spire.random.Generator

import axle.math.Σ
import axle.math.square
import axle.math.log2
import axle.algebra.RegionEq
import axle.algebra.Aggregatable
import axle.quanta.Information
import axle.quanta.InformationConverter
import axle.quanta.UnittedQuantity
import axle.probability._
import axle.syntax.kolmogorov.kolmogorovOps

package object stats {

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

  def expectation[A: Eq: Field: ConvertableTo, N: Field: ConvertableFrom](model: ConditionalProbabilityTable[A, N]): A = {

    def n2a(n: N): A = ConvertableFrom[N].toType[A](n)(ConvertableTo[A])

    Σ[A, IndexedSeq](model.domain.toVector.map { x =>
      n2a(model.P(RegionEq(x))) * x
    })
  }


  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def standardDeviation[A: Eq: NRoot: Field: ConvertableTo, N: Field: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A = {

    def n2a(n: N): A = ConvertableFrom[N].toType[A](n)(ConvertableTo[A])

    val μ: A = Σ[A, IndexedSeq](model.domain.toVector.map({ x => n2a(model.P(RegionEq(x))) * x }))

    val sum: A = Σ[A, IndexedSeq](model.domain.toVector map { x => n2a(model.P(RegionEq(x))) * square(x - μ) })

    NRoot[A].sqrt(sum)
  }

  def σ[A: Eq: NRoot: Field: ConvertableTo, N: Field: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A =
    standardDeviation[A, N](model)

  def stddev[ A: Eq: NRoot: Field: ConvertableTo, N: Field: ConvertableFrom](
    model: ConditionalProbabilityTable[A, N]): A =
    standardDeviation[A, N](model)

  def entropy[A: Eq, N: Field: Eq: ConvertableFrom](model: ConditionalProbabilityTable[A, N])(
    implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] = {

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

    val convertN = ConvertableFrom[N]
    val H = Σ[Double, Iterable](model.domain map { a: A =>
      val px: N = model.P(RegionEq(a))
      import cats.syntax.all._
      if (px === Field[N].zero) {
        0d
      } else {
        -convertN.toDouble(px) * log2(px)
      }
    })
    UnittedQuantity(H, convert.bit)
  }

  def H[A: Eq, N: Field: Eq: ConvertableFrom](model: ConditionalProbabilityTable[A, N])(
    implicit convert: InformationConverter[Double]): UnittedQuantity[Information, Double] =
    entropy(model)

  def _reservoirSampleK[N](k: Int, i: Int, reservoir: List[N], xs: LazyList[N], gen: Generator): LazyList[List[N]] =
    if (xs.isEmpty) {
      LazyList.cons(reservoir, LazyList.empty)
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
      LazyList.cons(newReservoir, _reservoirSampleK(k, i + 1, newReservoir, xs.tail, gen))
    }

  def reservoirSampleK[N](k: Int, xs: LazyList[N], gen: Generator) =
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
