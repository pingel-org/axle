package axle.stats

import cats.Show
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.dummy
import axle.math.Σ

object ConditionalProbabilityTable {

  implicit def showCPT[A: Show: Order, V: Show: Field](implicit prob: ProbabilityModel[ConditionalProbabilityTable]): Show[ConditionalProbabilityTable[A, V]] = cpt =>
    cpt.values.sorted.map(a => {
      val aString = Show[A].show(a)
      (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + Show[V].show(prob.probabilityOf(cpt, a)))
    }).mkString("\n")

  implicit val probabilityWitness: ProbabilityModel[ConditionalProbabilityTable] =
    new ProbabilityModel[ConditionalProbabilityTable] {

      def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): ConditionalProbabilityTable[A, V] =
        ConditionalProbabilityTable[A, V](as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: ConditionalProbabilityTable[A, _]): IndexedSeq[A] =
        model.values

      def combine[A, V](modelsToProbabilities: Map[ConditionalProbabilityTable[A, V], V])(implicit fieldV: Field[V]): ConditionalProbabilityTable[A, V] = {

        val parts: IndexedSeq[(A, V)] =
          modelsToProbabilities.toVector flatMap {
            case (model, weight) =>
              values(model).map(v => (v, probabilityOf(model, v) * weight))
          }

        val newDist: Map[A, V] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldV.plus)).toMap

        val v = modelsToProbabilities.headOption.map(_._1.variable).getOrElse(Variable[A]("?"))

        ConditionalProbabilityTable[A, V](newDist, v)
      }

      def conditionExpression[A, B, V](model: ConditionalProbabilityTable[A, V], predicate: A => Boolean, screen: A => B)(implicit fieldV: Field[V]): ConditionalProbabilityTable[B, V] = {
        val newMap: Map[B, V] = model.p.toVector.filter({ case (a, v) => predicate(a)}).map({ case (a, v) => screen(a) -> v }).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        ConditionalProbabilityTable[B, V](newMap.mapValues(v => v / newDenominator), Variable[B]("B"))
      }

      def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): ConditionalProbabilityTable[A, V] =
        ConditionalProbabilityTable(Map.empty, variable)

      def observe[A, V](model: ConditionalProbabilityTable[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      def probabilityOf[A, V](model: ConditionalProbabilityTable[A, V], a: A)(implicit fieldV: Field[V]): V =
        model.p.get(a).getOrElse(fieldV.zero)

      def probabilityOfExpression[A, V](model: ConditionalProbabilityTable[A, V], predicate: A => Boolean)(implicit fieldV: Field[V]): V =
        Σ(model.values.filter(predicate).map(model.p))
  }


}

case class ConditionalProbabilityTable[A, V](
  p:        Map[A, V],
  variable: Variable[A])(implicit ringV: Ring[V]) {

  val bars = p.toVector.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  def values: IndexedSeq[A] = p.keys.toVector

  def charWidth(implicit sa: Show[A]): Int =
    (values.map(a => Show[A].show(a).length).toList).reduce(math.max)

}
