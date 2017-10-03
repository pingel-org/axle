package axle.stats

// import spire.algebra.Field
// import spire.implicits.additiveGroupOps
// import axle.math.Π

import spire.algebra.AdditiveMonoid
import spire.random.Generator
import spire.random.Dist

trait Probability[M, A, N] {

  def apply(model: M, c: CaseIs[A]): N

  def values(model: M, variable: Variable[A]): IndexedSeq[A]

  def combine(variable: Variable[A], modelsToProbabilities: Map[M, N]): M

  def observe(model: M, gen: Generator)(implicit rng: Dist[N]): A

  def probabilityOf(model: M, a: A): N
}

trait ProbabilityGiven1[M, A, G, N] extends Function2[M, CaseGiven[A, G], N] {

  def values(model: M, variable: Variable[A]): IndexedSeq[A]
}

trait ProbabilityGiven2[M, A, G1, G2, N]
extends Function2[M, CaseGiven2[A, G1, G2], N] {

  def values(model: M, variable: Variable[A]): IndexedSeq[A]
}

object Probability {

  trait Monad[T]

  def monad[M, A, N](variable: Variable[A])(implicit prob: Probability[M, A, N], addition: AdditiveMonoid[N]): Monad[M] =
    new Monad[M] {

      def unit(a: Any): M = ???

      // (CPT0[A, N], A => B) => CPT0[B, N]
      def map[B](model: M, f: A => B): M =
        unit(
          prob
          .values(model, variable)
          .map({ v => f(v) -> prob.probabilityOf(model, v) })
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(addition.plus)))

      def flatMap[B](model: M, f: A => M, vb: Variable[B]): M =
        unit(
          prob
          .values(model, variable)
          .flatMap(a => {
            val p = prob.probabilityOf(model, a)
            val subDistribution = f(a)
            prob.values(subDistribution, vb).map(b => {
              b -> (p * subDistribution.probabilityOf(b))
            })
          })
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(addition.plus)))

  }

}