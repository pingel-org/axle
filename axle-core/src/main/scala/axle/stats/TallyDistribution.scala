package axle.stats

import cats.Show
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring

import spire.implicits.additiveSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.dummy
import axle.algebra.Region
import axle.algebra.RegionEq

object TallyDistribution {

  implicit def show[A: Order: Show, V: Show: Field]: Show[TallyDistribution[A, V]] = td =>
    td.values.sorted.map(a => {
      val aString = Show[A].show(a)
      // (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
      (aString + " " + Show[V].show(probabilityWitness.probabilityOf(td)(RegionEq(a))))
    }).mkString("\n")


    implicit val probabilityWitness: ProbabilityModel[TallyDistribution] =
    new ProbabilityModel[TallyDistribution] {
  
      def map[A, B, V](model: TallyDistribution[A, V])(f: A => B)(implicit eqB: cats.kernel.Eq[B]): TallyDistribution[B, V] = {
        import model.ringV
        TallyDistribution[B, V](
          model.tally.map({ case (a, v) => f(a) -> v }) // TODO use eqA to unique
        )
      }

      def redistribute[A: cats.kernel.Eq, V: Ring](model: TallyDistribution[A, V])(
        from: A, to: A, mass: V): TallyDistribution[A, V] =
        TallyDistribution(model.tally.map({ case (a, v) =>
          if(a === from) {
            a -> (v - mass)
          } else if (a === to) {
            a -> (v + mass)
          } else {
            a -> v
          }
        }))
  
      def flatMap[A, B, V](model: TallyDistribution[A, V])(f: A => TallyDistribution[B, V])(implicit eqB: cats.kernel.Eq[B]): TallyDistribution[B, V] = {
        val p = model.values.toVector.flatMap { a =>
          val tallyA = model.tally.apply(a)
          val inner = f(a)
          inner.values.toVector.map { b =>
            b -> model.ringV.times(tallyA, inner.tally.apply(b))
          }
        }.groupBy(_._1).map({ case (b, bvs) => b -> bvs.map(_._2).reduce(model.ringV.plus)})
        import model.ringV
        TallyDistribution(p)
      }
  
      def filter[A, V](model: TallyDistribution[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): TallyDistribution[A, V] = {
        val newMap: Map[A, V] = model.tally.toVector.filter({ case (a, v) => predicate(a)}).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        TallyDistribution[A, V](newMap.mapValues(v => v / newDenominator))
      }

      def unit[A, V](a: A)(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): TallyDistribution[A, V] =
        TallyDistribution(Map(a -> ringV.one))

      def observe[A, V](model: TallyDistribution[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = model.totalCount * gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // or distribution is malformed
      }

      def probabilityOf[A, V](model: TallyDistribution[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V =
        Σ(model.values.filter(predicate).map(model.tally)) / model.totalCount

    }

}
case class TallyDistribution[A, V](
  tally:    Map[A, V])(implicit val ringV: Ring[V]) {

  val values: IndexedSeq[A] = tally.keys.toVector

  val totalCount: V = Σ[V, Iterable](tally.values)

  val bars: Map[A, V] =
    tally.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, ringV.plus(x._2, y._2))).drop(1)

}
