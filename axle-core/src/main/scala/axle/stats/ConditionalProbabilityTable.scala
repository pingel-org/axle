package axle.stats

import cats.Show
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Field
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.rng.Cmwc5
import axle.string
import axle.dummy

object ConditionalProbabilityTable0 {

  implicit def showCPT[A: Show: Order, N: Show]: Show[ConditionalProbabilityTable0[A, N]] =
    new Show[ConditionalProbabilityTable0[A, N]] {

      def show(cpt: ConditionalProbabilityTable0[A, N]): String =
        cpt.values.sorted.map(a => {
          val aString = string(a)
          (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(cpt.probabilityOf(a)))
        }).mkString("\n")
    }

}

case class ConditionalProbabilityTable0[A, N: Field: Order: Dist](p: Map[A, N]) {

  val field = Field[N]

  def map[B](f: A => B): ConditionalProbabilityTable0[B, N] =
    ConditionalProbabilityTable0[B, N](
      values
        .map({ v => f(v) -> probabilityOf(v) })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(field.plus)))

  def flatMap[B](f: A => ConditionalProbabilityTable0[B, N]): ConditionalProbabilityTable0[B, N] =
    ConditionalProbabilityTable0[B, N](
      values
        .flatMap(a => {
          val p = probabilityOf(a)
          val subDistribution = f(a)
          subDistribution.values.map(b => {
            b -> (p * subDistribution.probabilityOf(b))
          })
        })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(field.plus)))

  val bars = p.scanLeft((dummy[A], field.zero))((x, y) => (y._1, x._2 + y._2)).drop(1)

  val rng = Cmwc5()

  val order = Order[N]

  def observe(): A = {
    val r = rng.next[N]
    bars.find({ case (_, v) => order.gteqv(v, r) }).get._1 // otherwise malformed distribution
  }

  def values: IndexedSeq[A] = p.keys.toVector

  def probabilityOf(a: A): N = p.get(a).getOrElse(field.zero)

  def charWidth(implicit sa: Show[A]): Int =
    (values.map(a => string(a).length).toList).reduce(math.max)

}

case class ConditionalProbabilityTable2[A, G1, G2, N: Field: Order](
    p: Map[(G1, G2), Map[A, N]],
    val name: String = "unnamed") {

  lazy val _values = p.values.map(_.keySet).reduce(_ union _).toVector

  def values: IndexedSeq[A] = _values

}
