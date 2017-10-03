package axle.stats

// import spire.algebra.Field
// import spire.implicits.additiveGroupOps
// import axle.math.Π
// import spire.algebra.AdditiveMonoid
import spire.random.Generator
import spire.random.Dist

trait Probability[M[_], A, N] {

  def values(model: M[A]): IndexedSeq[A]

  def combine(modelsToProbabilities: Map[M[A], N]): M[A]

  def condition[G](model: M[A], given: CaseIs[G]): M[A]

  def empty[B]: M[B]

  def orientation(model: M[A]): Variable[A]

  def orient[B](model: M[A], newVariable: Variable[B]): M[B]

  def observe(model: M[A], gen: Generator)(implicit rng: Dist[N]): A

  def probabilityOf(model: M[A], a: A): N
}

object Probability {

  /*
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
  */
}