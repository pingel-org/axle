package axle.stats

import cats.Show
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.dummy

object ConditionalProbabilityTable0 {

  implicit def showCPT[A: Show: Order, V: Show: Field](implicit prob: ProbabilityModel[ConditionalProbabilityTable0]): Show[ConditionalProbabilityTable0[A, V]] = cpt =>
    cpt.values.sorted.map(a => {
      val aString = Show[A].show(a)
      (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + Show[V].show(prob.probabilityOf(cpt, a)))
    }).mkString("\n")

  implicit val probabilityWitness: ProbabilityModel[ConditionalProbabilityTable0] =
    new ProbabilityModel[ConditionalProbabilityTable0] {

      def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): ConditionalProbabilityTable0[A, V] =
        ConditionalProbabilityTable0[A, V](as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: ConditionalProbabilityTable0[A, _]): IndexedSeq[A] =
        model.values

      def combine[A, V](modelsToProbabilities: Map[ConditionalProbabilityTable0[A, V], V])(implicit fieldV: Field[V]): ConditionalProbabilityTable0[A, V] = {

        val parts: IndexedSeq[(A, V)] =
          modelsToProbabilities.toVector flatMap {
            case (model, weight) =>
              values(model).map(v => (v, probabilityOf(model, v) * weight))
          }

        val newDist: Map[A, V] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldV.plus)).toMap

        val v = modelsToProbabilities.headOption.map({ case (m, _) => orientation(m) }).getOrElse(Variable[A]("?"))

        ConditionalProbabilityTable0[A, V](newDist, v)
      }

      def condition[A, V, G](model: ConditionalProbabilityTable0[A, V], given: CaseIs[G]): ConditionalProbabilityTable0[A, V] =
        model // TODO true unless G =:= A and model.variable === variable

      def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): ConditionalProbabilityTable0[A, V] =
        ConditionalProbabilityTable0(Map.empty, variable)

      def orientation[A, V](model: ConditionalProbabilityTable0[A, V]): Variable[A] =
        model.variable

      def orient[A, B, V](model: ConditionalProbabilityTable0[A, V], newVariable: Variable[B])(implicit ringV: Ring[V]): ConditionalProbabilityTable0[B, V] =
        empty(newVariable)

      def observe[A, V](model: ConditionalProbabilityTable0[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      def probabilityOf[A, V](model: ConditionalProbabilityTable0[A, V], a: A)(implicit fieldV: Field[V]): V =
        model.p.get(a).getOrElse(fieldV.zero)
    }

}

case class ConditionalProbabilityTable0[A, V](
  p:        Map[A, V],
  variable: Variable[A])(implicit ringV: Ring[V]) {

  val bars = p.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  def values: IndexedSeq[A] = p.keys.toVector

  def charWidth(implicit sa: Show[A]): Int =
    (values.map(a => Show[A].show(a).length).toList).reduce(math.max)

}

case class ConditionalProbabilityTable2[A, G1, G2, N: Field: Order](
  p:        Map[(G1, G2), Map[A, N]],
  variable: Variable[A]) {

  lazy val _values = p.values.map(_.keySet).reduce(_ union _).toVector

  def values: IndexedSeq[A] = _values

}
