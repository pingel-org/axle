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

      def sum[A, V1, V2](model: TallyDistribution[A, V1])(other: TallyDistribution[A, V2])(implicit fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): TallyDistribution[A, (V1, V2)] = {

        val newValues = (model.tally.keySet ++ other.tally.keySet).toVector // TODO should unique by Eq[A], not universal equality
  
        implicit val fieldV12: Field[(V1, V2)] = axle.algebra.tuple2Field[V1, V2](fieldV1, fieldV2, eqV1, eqV2)
  
        construct[A, (V1, V2)](model.variable, newValues, a => (probabilityOf(model, a), probabilityOf(other, a)))
      }
  
      def product[A, B, V](model: TallyDistribution[A, V])(other: TallyDistribution[B, V])(implicit fieldV: Field[V]): TallyDistribution[(A, B), V] = {
        val abvMap: Map[(A, B), V] = (for {
          a <- values(model)
          b <- values(other)
        } yield ((a, b) -> (probabilityOf(model, a) * probabilityOf(other, b)))).toMap
        construct[(A, B), V](Variable[(A, B)](model.variable.name + " " + other.variable.name), abvMap.keys, abvMap)
      }
  
      def mapValues[A, V, V2](model: TallyDistribution[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): TallyDistribution[A, V2] =
        construct[A, V2](model.variable, model.tally.keys, (a: A) => f(model.tally(a)))
  
      def conditionExpression[A, B, V](model: TallyDistribution[A, V], predicate: A => Boolean, screen: A => B)(implicit fieldV: Field[V]): TallyDistribution[B, V] = {
        val newMap: Map[B, V] = model.tally.toVector.filter({ case (a, v) => predicate(a)}).map({ case (a, v) => screen(a) -> v }).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        TallyDistribution[B, V](newMap.mapValues(v => v / newDenominator), Variable[B]("B"))
      }

      def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): TallyDistribution[A, V] =
        TallyDistribution(Map.empty, variable)

      def observe[A, V](model: TallyDistribution[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = model.totalCount * gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // or distribution is malformed
      }

      def probabilityOf[A, V](model: TallyDistribution[A, V], a: A)(implicit fieldV: Field[V]): V =
        model.tally.get(a).getOrElse(fieldV.zero) / model.totalCount

      def probabilityOfExpression[A, V](model: TallyDistribution[A, V], predicate: A => Boolean)(implicit fieldV: Field[V]): V =
        Σ(model.values.filter(predicate).map(model.tally)) / model.totalCount

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
