package axle.stats

import cats.Show
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

import spire.algebra.Field
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.string
import axle.dummy

object ConditionalProbabilityTable0 {

  implicit def showCPT[A: Show: Order, N: Show](implicit prob: Probability[({ type λ[T] = ConditionalProbabilityTable0[T, N] })#λ, N]): Show[ConditionalProbabilityTable0[A, N]] =
    new Show[ConditionalProbabilityTable0[A, N]] {

      def show(cpt: ConditionalProbabilityTable0[A, N]): String =
        cpt.values.sorted.map(a => {
          val aString = string(a)
          (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(prob.probabilityOf(cpt, a)))
        }).mkString("\n")
    }

  implicit def probability[N](implicit fieldN: Field[N], orderN: Order[N]): Probability[({ type λ[T] = ConditionalProbabilityTable0[T, N] })#λ, N] =
    new Probability[({ type λ[T] = ConditionalProbabilityTable0[T, N] })#λ, N] {

      def construct[A](variable: Variable[A], as: Iterable[A], f: A => N): ConditionalProbabilityTable0[A, N] =
        ConditionalProbabilityTable0(as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: ConditionalProbabilityTable0[A, N]): IndexedSeq[A] =
        model.values

      def combine[A](modelsToProbabilities: Map[ConditionalProbabilityTable0[A, N], N]): ConditionalProbabilityTable0[A, N] = {

        val parts: IndexedSeq[(A, N)] =
          modelsToProbabilities.toVector flatMap { case (model, weight) =>
            values(model).map(v => (v, probabilityOf(model, v) * weight))
          }

        val newDist: Map[A, N] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldN.plus)).toMap

        val v = modelsToProbabilities.headOption.map({ case (m, _) => orientation(m)}).getOrElse(Variable[A]("?"))

        ConditionalProbabilityTable0[A, N](newDist, v)
      }

      def condition[A, G](model: ConditionalProbabilityTable0[A, N], given: CaseIs[G]): ConditionalProbabilityTable0[A, N] =
        model // TODO true unless G =:= A and model.variable === variable

      def empty[A](variable: Variable[A]): ConditionalProbabilityTable0[A, N] =
        ConditionalProbabilityTable0(Map.empty, variable)

      def orientation[A](model: ConditionalProbabilityTable0[A, N]): Variable[A] =
        model.variable

      def orient[A, B](model: ConditionalProbabilityTable0[A, N], newVariable: Variable[B]): ConditionalProbabilityTable0[B, N] =
        empty[B](newVariable)

      def observe[A](model: ConditionalProbabilityTable0[A, N], gen: Generator)(implicit rng: Dist[N]): A = {
        val r: N = rng.apply(gen)
        model.bars.find({ case (_, v) => model.order.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      def probabilityOf[A](model: ConditionalProbabilityTable0[A, N], a: A): N =
        model.p.get(a).getOrElse(model.field.zero)
    }

}

case class ConditionalProbabilityTable0[A, N: Field: Order](
    p: Map[A, N],
    variable: Variable[A]) {

  val field = Field[N]
  val order = Order[N]

  val bars = p.scanLeft((dummy[A], field.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  def values: IndexedSeq[A] = p.keys.toVector

  def charWidth(implicit sa: Show[A]): Int =
    (values.map(a => string(a).length).toList).reduce(math.max)

}

case class ConditionalProbabilityTable2[A, G1, G2, N: Field: Order](
    p: Map[(G1, G2), Map[A, N]],
    val name: String = "unnamed") {

  lazy val _values = p.values.map(_.keySet).reduce(_ union _).toVector

  def values: IndexedSeq[A] = _values

}
