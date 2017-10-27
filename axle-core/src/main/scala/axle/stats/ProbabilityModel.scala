package axle.stats

/**
 * ProbabilityModel
 * 
 * See http://www.stat.yale.edu/Courses/1997-98/101/probint.htm
 */

import spire.random.Generator
import spire.random.Dist

trait ProbabilityModel[M[_], N] {

  def construct[A](variable: Variable[A], as: Iterable[A], f: A => N): M[A]

  def values[A](model: M[A]): IndexedSeq[A]

  def combine[A](modelsToProbabilities: Map[M[A], N]): M[A]

  def empty[A](variable: Variable[A]): M[A]

  def orientation[A](model: M[A]): Variable[A]

  def orient[A, B](model: M[A], newVariable: Variable[B]): M[B]

  def probabilityOf[A](model: M[A], a: A): N

  def condition[A, G](model: M[A], given: CaseIs[G]): M[A]

  def observe[A](model: M[A], gen: Generator)(implicit spireDist: Dist[N]): A

}

object ProbabilityModel {

  import cats._
  import spire.algebra.Field

  implicit def monad[M[_], N](implicit prob: ProbabilityModel[M, N], fieldN: Field[N]): Monad[M] =
    new Monad[M] {

      def pure[A](a: A): M[A] =
        prob.construct(Variable[A]("a"), Vector(a), (a: A) => Field[N].one)

      def tailRecM[A, B](a: A)(f: A => M[Either[A,B]]): M[B] =
       ???

      override def map[A, B](model: M[A])(f: A => B): M[B] = {

        val b2n = prob
          .values(model)
          .map({ v => f(v) -> prob.probabilityOf(model, v) })
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(fieldN.plus))

        prob.construct(Variable[B]("b"), b2n.keys, b2n)
      }

      override def flatMap[A, B](model: M[A])(f: A => M[B]): M[B] = {

        val foo = prob.values(model)
          .flatMap(a => {
            val p = prob.probabilityOf(model, a)
            val subDistribution = f(a)
            prob.values(subDistribution).map(b => {
              b -> (fieldN.times(p, prob.probabilityOf(subDistribution, b)))
            })
          })

        val b2n =
          foo
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(fieldN.plus))

        prob.construct(Variable[B]("b"), b2n.keys, b2n)
      }
  }

}