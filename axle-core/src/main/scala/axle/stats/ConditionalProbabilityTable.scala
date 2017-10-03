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

  implicit def showCPT[A: Show: Order, N: Show](implicit prob: Probability[ConditionalProbabilityTable0[A, N], A, N]): Show[ConditionalProbabilityTable0[A, N]] =
    new Show[ConditionalProbabilityTable0[A, N]] {

      def show(cpt: ConditionalProbabilityTable0[A, N]): String =
        cpt.values.sorted.map(a => {
          val aString = string(a)
          (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(prob.probabilityOf(cpt, a)))
        }).mkString("\n")
    }

  implicit def probability[A, N](implicit fieldN: Field[N], orderN: Order[N], distN: Dist[N]): Probability[ConditionalProbabilityTable0[A, N], A, N] =
    new Probability[ConditionalProbabilityTable0[A, N], A, N] {

      def apply(model: ConditionalProbabilityTable0[A, N], c: CaseIs[A]): N =
        if(c.is) {
          probabilityOf(model, c.value)
        } else {
          fieldN.minus(fieldN.one, probabilityOf(model, c.value))
        }

      def values(model: ConditionalProbabilityTable0[A, N], variable: Variable[A]): IndexedSeq[A] =
        model.values

      def combine(variable: Variable[A], modelsToProbabilities: Map[ConditionalProbabilityTable0[A, N], N]): ConditionalProbabilityTable0[A, N] = {

        val parts: IndexedSeq[(A, N)] =
          modelsToProbabilities.toVector flatMap { case (model, weight) =>
            values(model, variable).map(v => (v, apply(model, CaseIs(v, variable)) * weight))
          }

        val newDist: Map[A, N] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldN.plus)).toMap

        ConditionalProbabilityTable0[A, N](newDist)
      }

      def observe(model: ConditionalProbabilityTable0[A, N], gen: Generator)(implicit rng: Dist[N]): A = {
        val r: N = rng.apply(gen)
        model.bars.find({ case (_, v) => model.order.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      def probabilityOf(model: ConditionalProbabilityTable0[A, N], a: A): N =
        model.p.get(a).getOrElse(model.field.zero)

    }

}

case class ConditionalProbabilityTable0[A, N: Field: Order](val p: Map[A, N]) {

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
