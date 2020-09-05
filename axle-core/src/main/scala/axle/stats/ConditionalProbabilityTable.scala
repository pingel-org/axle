package axle.stats

import cats.Show
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeGroupOps
import spire.random.Dist
import spire.random.Generator

import axle.dummy
import axle.math.Σ
import axle.algebra._

object ConditionalProbabilityTable {

  implicit def showCPT[A: Show: Order, V: Show: Field](implicit prob: ProbabilityModel[ConditionalProbabilityTable], showRA: Show[Region[A]]): Show[ConditionalProbabilityTable[A, V]] = cpt =>
    cpt.values.toVector.sorted.map { a => {
      val ra = RegionEq(a)
      val aString = showRA.show(ra)
      (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + Show[V].show(prob.probabilityOf(cpt)(ra)))
    }}.mkString("\n")

  // ({ type λ[V] = ConditionalProbabilityTable[A, V] })#λ
  implicit val probabilityWitness: ProbabilityModel[ConditionalProbabilityTable] =
    new ProbabilityModel[ConditionalProbabilityTable] {

      def map[A, B, V](model: ConditionalProbabilityTable[A, V])(f: A => B)(implicit eqB: cats.kernel.Eq[B]): ConditionalProbabilityTable[B, V] = {
        import model.ringV
        ConditionalProbabilityTable[B, V](
          model.p.iterator.map({ case (a, v) =>
             f(a) -> v
          }).toVector.groupBy(_._1).map({ case (b, bvs) =>
             b -> bvs.map(_._2).reduce(model.ringV.plus)
          }).toMap) // TODO use eqA to unique
      }

      def flatMap[A, B, V](model: ConditionalProbabilityTable[A, V])(f: A => ConditionalProbabilityTable[B, V])(implicit eqB: cats.kernel.Eq[B]): ConditionalProbabilityTable[B, V] = {
        val p = model.values.toVector.flatMap { a =>
          val pA = model.p.apply(a)
          val inner = f(a)
          inner.values.toVector.map { b =>
            b -> model.ringV.times(pA, inner.p.apply(b))
          }
        }.groupBy(_._1).map({ case (b, bvs) => b -> bvs.map(_._2).reduce(model.ringV.plus)})
        import model.ringV
        ConditionalProbabilityTable(p)
      }

      def filter[A, V](model: ConditionalProbabilityTable[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): ConditionalProbabilityTable[A, V] = {
        val newMap: Map[A, V] = model.p.toVector.filter({ case (a, v) => predicate(a)}).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        import model.eqA
        ConditionalProbabilityTable[A, V](newMap.view.mapValues(v => v / newDenominator).toMap)
      }

      def unit[A, V](a: A)(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): ConditionalProbabilityTable[A, V] =
        ConditionalProbabilityTable(Map(a -> ringV.one))

      def observe[A, V](model: ConditionalProbabilityTable[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      def probabilityOf[A, V](model: ConditionalProbabilityTable[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V =
        predicate match {
          case RegionEmpty() =>
            fieldV.zero
          case RegionAll() =>
            fieldV.one
          case _ => 
            Σ(model.values.map { a =>
              if (predicate(a)) { model.p(a) } else { fieldV.zero }
            })
        }

  }

}

case class ConditionalProbabilityTable[A, V](
  p: Map[A, V])(
  implicit
  val ringV: Ring[V],
  val eqA: cats.kernel.Eq[A]) {

  val bars = p.toVector.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  def values: Iterable[A] = p.keys.toVector

  def charWidth(implicit showRA: Show[Region[A]]): Int =
    (values.map(ra => showRA.show(RegionEq(ra)).length).toList).reduce(math.max)

}
