package axle.stats

import scala.util.Random

import cats.Show
import cats.implicits.catsSyntaxEq
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps

import axle.math.Σ
import axle.string
import axle.dummy

object TallyDistribution0 {

  implicit def show[A: Order: Show, N: Show]: Show[TallyDistribution0[A, N]] =
    new Show[TallyDistribution0[A, N]] {

      def show(td: TallyDistribution0[A, N]): String =
        td.name + "\n" +
          td.values.sorted.map(a => {
            val aString = string(a)
            (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
          }).mkString("\n")
    }

}

case class TallyDistribution0[A, N: Field: Order](
    val tally: Map[A, N],
    val name: String = "unnamed") {

  val ring = Ring[N]
  val addition = implicitly[AdditiveMonoid[N]]

  def values: IndexedSeq[A] = tally.keys.toVector

  def map[B](f: A => B): TallyDistribution0[B, N] =
    TallyDistribution0(
      values
        .map({ v => f(v) -> probabilityOf(v) })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(addition.plus)))

  def flatMap[B](f: A => Distribution0[B, N]): TallyDistribution0[B, N] =
    TallyDistribution0(
      values
        .flatMap(a => {
          val p = probabilityOf(a)
          val subDistribution = f(a)
          subDistribution.values.map(b => {
            b -> (p * subDistribution.probabilityOf(b))
          })
        })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(addition.plus)))

  val totalCount: N = Σ[N, Iterable[N]](tally.values)

  val bars: Map[A, N] =
    tally.scanLeft((dummy[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2))).drop(1)

  val order = Order[N]

  def observe(): A = {
    val r: N = totalCount * Random.nextDouble()
    bars.find({ case (_, v) => order.gteqv(v, r) }).get._1 // or distribution is malformed
  }

  def probabilityOf(a: A): N = tally.get(a).getOrElse(ring.zero) / totalCount

}

object TallyDistribution1 {

  def probabilityOf(a: A): N =
    Σ[N, Iterable[N]](gvs.map(gv => tally((a, gv)))) / totalCount

  def probabilityOf(a: A, given: Case[G]): N = given match {
    case CaseIs(argGrv, gv)   => tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2))
    case CaseIsnt(argGrv, gv) => 1 - (tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2)))
    case _                    => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

case class TallyDistribution1[A, G: Eq, N: Field: Order](
    val tally: Map[(A, G), N],
    _name: String = "unnamed") {

  def name: String = _name

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector

  def values: IndexedSeq[A] = _values

  val gvs = tally.keys.map(_._2).toSet

  val totalCount: N = Σ[N, Iterable[N]](tally.values)
}
