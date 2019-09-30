package axle.stats

import cats.Show
import cats.kernel.Order
//import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
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

      def adjoin[A, V1, V2](
        model: ConditionalProbabilityTable[A, V1])(
        other: ConditionalProbabilityTable[A, V2])(
        implicit
        eqA: cats.kernel.Eq[A],
        fieldV1: Field[V1], fieldV2: Field[V2],
        eqV1: cats.kernel.Eq[V1],
        eqV2: cats.kernel.Eq[V2]): ConditionalProbabilityTable[A, (V1, V2)] = {

        val newValues = (model.p.keySet ++ other.p.keySet) // TODO should unique by Eq[A], not universal equality

        implicit val fieldV12: Field[(V1, V2)] = axle.algebra.tuple2Field[V1, V2](fieldV1, fieldV2, eqV1, eqV2)

        ConditionalProbabilityTable[A, (V1, V2)](
          newValues.map( key => {
            val valuePair = (probabilityOf(model)(RegionEq(key)), probabilityOf(other)(RegionEq(key)))
            key -> valuePair
          }).toMap,
          //  RegionSet(newValues),
          // { a: Region[A] => (probabilityOfExpression(model)(a), probabilityOfExpression(other)(a)) },
          model.variable)
      }

      def chain[A, B, V]
        (model: ConditionalProbabilityTable[A, V])
        (other: ConditionalProbabilityTable[B, V])
        (implicit fieldV: Field[V], eqA: cats.kernel.Eq[A], eqB: cats.kernel.Eq[B]): ConditionalProbabilityTable[(A, B), V] = {

        val abvMap: Map[(A, B), V] = (
          for {
            a <- model.values
            b <- other.values
          } yield {
            val v: V = probabilityOf(model)(RegionEq(a)) * probabilityOf(other)(RegionEq(b))
            (a, b) -> v
          }
        ).toMap

        ConditionalProbabilityTable[(A, B), V](
          abvMap,
          Variable[(A, B)](model.variable.name + " " + other.variable.name))
      }

      def map[A, B, V](model: ConditionalProbabilityTable[A, V])(f: A => B)(implicit eqB: cats.kernel.Eq[B]): ConditionalProbabilityTable[B, V] = {
        import model.ringV
        ConditionalProbabilityTable[B, V](
          model.p.iterator.map({ case (a, v) =>
             f(a) -> v
          }).toVector.groupBy(_._1).map({ case (b, bvs) =>
             b -> bvs.map(_._2).reduce(model.ringV.plus)
          }).toMap, // TODO use eqA to unique
          Variable[B](model.variable.name + "'"))
      }

      def redistribute[A: cats.kernel.Eq, V: Ring](model: ConditionalProbabilityTable[A, V])(
        from: A, to: A, mass: V): ConditionalProbabilityTable[A, V] =
        ConditionalProbabilityTable(model.p.map({ case (a, v) =>
          if(a === from) {
            a -> (v - mass)
          } else if (a === to) {
            a -> (v + mass)
          } else {
            a -> v
          }
         }), model.variable)

      def mapValues[A, V, V2](model: ConditionalProbabilityTable[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): ConditionalProbabilityTable[A, V2] = {
        import model.eqA
        ConditionalProbabilityTable[A, V2](
          model.p.mapValues(f),
          model.variable)
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
        ConditionalProbabilityTable(p, Variable[B]("?"))
      }

      def filter[A, V](model: ConditionalProbabilityTable[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): ConditionalProbabilityTable[A, V] = {
        val newMap: Map[A, V] = model.p.toVector.filter({ case (a, v) => predicate(a)}).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        import model.eqA
        ConditionalProbabilityTable[A, V](newMap.mapValues(v => v / newDenominator), model.variable)
      }

      def unit[A, V](a: A, variable: Variable[A])(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): ConditionalProbabilityTable[A, V] = {
        ConditionalProbabilityTable(Map(a -> ringV.one), variable)
      }

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
  p:        Map[A, V],
  variable: Variable[A])(implicit val ringV: Ring[V], val eqA: cats.kernel.Eq[A]) {

  val bars = p.toVector.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  def values: Iterable[A] = p.keys.toVector

  def charWidth(implicit showRA: Show[Region[A]]): Int =
    (values.map(ra => showRA.show(RegionEq(ra)).length).toList).reduce(math.max)

}
