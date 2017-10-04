package axle.stats

// import spire.algebra.Field
// import spire.implicits.additiveGroupOps
// import axle.math.Π
// import spire.algebra.AdditiveMonoid
import spire.random.Generator
import spire.random.Dist

trait Probability[M[_], N] {

  def values[A](model: M[A]): IndexedSeq[A]

  def combine[A](modelsToProbabilities: Map[M[A], N]): M[A]

  def condition[A, G](model: M[A], given: CaseIs[G]): M[A]

  def empty[A](variable: Variable[A]): M[A]

  def orientation[A](model: M[A]): Variable[A]

  def orient[A, B](model: M[A], newVariable: Variable[B]): M[B]

  def observe[A](model: M[A], gen: Generator)(implicit rng: Dist[N]): A

  def probabilityOf[A](model: M[A], a: A): N
}

object Probability {

  import cats._
  import spire.algebra.Field

  implicit def monad[M[_], N](implicit prob: Probability[M, N], fieldN: Field[N]): Monad[M] =
    new Monad[M] {

      def unit[B](a: Any): M[B] = ???

      override def map[A, B](model: M[A], f: A => B): M[B] =
        unit(
          prob
          .values(model)
          .map({ v => f(v) -> prob.probabilityOf(model, v) })
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(fieldN.plus)))

      override def flatMap[A, B](model: M[A], f: A => M[B]): M[B] =
        unit(
          prob
          .values(model)
          .flatMap(a => {
            val p = prob.probabilityOf(model, a)
            val subDistribution = f(a)
            prob.values(subDistribution).map(b => {
              b -> (fieldN.times(p, prob.probabilityOf(subDistribution, b)))
            })
          })
          .groupBy(_._1)
          .mapValues(_.map(_._2).reduce(fieldN.plus)))
  }

}