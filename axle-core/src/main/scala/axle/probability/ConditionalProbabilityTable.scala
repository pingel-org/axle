package axle.probability

import cats.Show
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeGroupOps
import spire.random.Dist
import spire.random.Generator

import axle.algebra.dummy
import axle.math.Σ
import axle.algebra._

case class ConditionalProbabilityTable[E, V: Ring](p: Map[E, V]) {

  def domain: Iterable[E] = p.keys.toVector

}


object ConditionalProbabilityTable {

  implicit def showCPT[
    A: Show: Order,
    V: Show: Field](
    implicit
    kolm: Kolmogorov[ConditionalProbabilityTable],
    showRA: Show[Region[A]]
  ): Show[ConditionalProbabilityTable[A, V]] = cpt => {
      val charWidth: Int = (cpt.domain.map(ra => showRA.show(RegionEq(ra)).length).toList).reduce(math.max)
      cpt.domain.toVector.sorted.map { a => {
        val ra = RegionEq(a)
        val aString = showRA.show(ra)
        (aString + (1 to (charWidth - aString.length)).map(i => " ").mkString("") + " " + Show[V].show(kolm.probabilityOf(cpt)(ra)))
      }}.mkString("\n")
    }

  implicit val bayesWitness: Bayes[ConditionalProbabilityTable] =
    new Bayes[ConditionalProbabilityTable] {

      def filter[A, V](model: ConditionalProbabilityTable[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): ConditionalProbabilityTable[A, V] = {
        val newMap: Map[A, V] = model.p.toVector.filter({ case (a, v) => predicate(a)}).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        ConditionalProbabilityTable[A, V](newMap.view.mapValues(v => v / newDenominator).toMap)
      }
  }

  implicit val samplerWitness: Sampler[ConditionalProbabilityTable] =
    new Sampler[ConditionalProbabilityTable]{

      def sample[A, V](model: ConditionalProbabilityTable[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        // implicit val ringV: Ring[V], val eqA: cats.kernel.Eq[A]
        // TODO cache the bars?
        val bars = model.p.toVector.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)
        val r: V = gen.next[V]
        bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }
  }

  implicit val kolmogorovWitness: Kolmogorov[ConditionalProbabilityTable] =
    new Kolmogorov[ConditionalProbabilityTable] {

      def probabilityOf[A, V](model: ConditionalProbabilityTable[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V =
        predicate match {
          case RegionEmpty() =>
            fieldV.zero
          case RegionAll() =>
            fieldV.one
          case _ => 
            Σ(model.domain.toVector.map { a =>
              if (predicate(a)) { model.p(a) } else { fieldV.zero }
            })
        }
    }

  import cats.Monad

  implicit def monadWitness[V: Ring]: Monad[ConditionalProbabilityTable[?, V]] =
    new Monad[ConditionalProbabilityTable[?, V]] {

      def pure[A](a: A): ConditionalProbabilityTable[A, V] =
        ConditionalProbabilityTable(Map(a -> Ring[V].one))

      override def map[A, B](
        model: ConditionalProbabilityTable[A, V])(
        f: A => B): ConditionalProbabilityTable[B, V] =
        ConditionalProbabilityTable[B, V](
          model.p.iterator.map({ case (a, v) =>
             f(a) -> v
          }).toVector.groupBy(_._1).map({ case (b, bvs) =>
             b -> bvs.map(_._2).reduce(Ring[V].plus)
          }).toMap) // TODO use eqA to unique

      def flatMap[A, B](model: ConditionalProbabilityTable[A, V])(f: A => ConditionalProbabilityTable[B, V]): ConditionalProbabilityTable[B, V] = {
        val p = model.domain.toVector.flatMap { a =>
          val pA = model.p.apply(a)
          val inner = f(a)
          inner.domain.toVector.map { b =>
            b -> Ring[V].times(pA, inner.p.apply(b))
          }
        }.groupBy(_._1).map({ case (b, bvs) => b -> bvs.map(_._2).reduce(Ring[V].plus)})
        ConditionalProbabilityTable(p)
      }

      def tailRecM[A, B](a: A)(f: A => ConditionalProbabilityTable[Either[A,B],V]): ConditionalProbabilityTable[B,V] = ???
  }

}
