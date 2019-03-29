package axle.stats

import cats.Show
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring

import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.dummy

object TallyDistribution {

  implicit def show[A: Order: Show, V: Show: Field]: Show[TallyDistribution[A, V]] = td =>
    td.values.sorted.map(a => {
      val aString = Show[A].show(a)
      // (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
      (aString + " " + Show[V].show(probabilityWitness.probabilityOf(td, a)))
    }).mkString("\n")


    implicit val probabilityWitness: ProbabilityModel[TallyDistribution] =
    new ProbabilityModel[TallyDistribution] {

      def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): TallyDistribution[A, V] =
        TallyDistribution[A, V](as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: TallyDistribution[A, _]): IndexedSeq[A] =
        model.values

      def combine[A, V](modelsToProbabilities: Map[TallyDistribution[A, V], V])(implicit fieldV: Field[V]): TallyDistribution[A, V] = {

        // TODO assert that all models are oriented for same Variable[A]

        val parts: IndexedSeq[(A, V)] =
          modelsToProbabilities.toVector flatMap {
            case (model, weight) =>
              values(model).map(v => (v, model.tally.get(v).getOrElse(fieldV.zero) * weight))
          }

        val newDist: Map[A, V] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldV.plus)).toMap

        val v = modelsToProbabilities.headOption.map(_._1.variable).getOrElse(Variable[A]("?"))

        TallyDistribution[A, V](newDist, v)
      }

      def condition[A, V](model: TallyDistribution[A, V], given: A): TallyDistribution[A, V] =
        model // TODO true unless G =:= A and model.variable === variable

      def conditionExpression[A, B, V](model: TallyDistribution[A, V], predicate: A => Boolean, screen: A => B): TallyDistribution[B, V] =
        ???

      def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): TallyDistribution[A, V] =
        TallyDistribution(Map.empty, variable)

      def observe[A, V](model: TallyDistribution[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = model.totalCount * gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // or distribution is malformed
      }

      def probabilityOf[A, V](model: TallyDistribution[A, V], a: A)(implicit fieldV: Field[V]): V =
        model.tally.get(a).getOrElse(fieldV.zero) / model.totalCount

      def probabilityOfExpression[A, V](model: TallyDistribution[A, V], f: A => Boolean)(implicit fieldV: Field[V]): V =
        ???

    }

}
case class TallyDistribution[A, V](
  tally:    Map[A, V],
  variable: Variable[A])(implicit ring: Ring[V]) {

  val values: IndexedSeq[A] = tally.keys.toVector

  val totalCount: V = Σ[V, Iterable](tally.values)

  val bars: Map[A, V] =
    tally.scanLeft((dummy[A], ring.zero))((x, y) => (y._1, ring.plus(x._2, y._2))).drop(1)

}
