package axle.stats

import scala.util.Random

import axle.algebra.Σ
import axle.string
import cats.Show
import cats.implicits.catsSyntaxEq
import cats.kernel.Eq
import cats.kernel.Order
import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps

object TallyDistribution0 {

  implicit def show[A: Order: Show, N: Show]: Show[TallyDistribution0[A, N]] = new Show[TallyDistribution0[A, N]] {

    implicit val orderingA = Order[A].toOrdering

    def show(td: TallyDistribution0[A, N]): String =
      td.name + "\n" +
        td.values.sorted.map(a => {
          val aString = string(a)
          (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
        }).mkString("\n")
  }

}

case class TallyDistribution0[A, N: Field: Order](val tally: Map[A, N], val name: String = "unnamed")
    extends Distribution0[A, N] {

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

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)

  val totalCount: N = Σ[N, Iterable[N]](tally.values)

  val bars: Map[A, N] =
    tally.scanLeft((null.asInstanceOf[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2)))

  val order = Order[N]

  def observe(): A = {
    val r: N = totalCount * Random.nextDouble()
    bars.find({ case (_, v) => order.gt(v, r) }).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): N = tally.get(a).getOrElse(ring.zero) / totalCount

}

case class TallyDistribution1[A, G: Eq, N: Field: Order](val tally: Map[(A, G), N], _name: String = "unnamed")
    extends Distribution1[A, G, N] {

  def name: String = _name

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector

  def values: IndexedSeq[A] = _values

  val gvs = tally.keys.map(_._2).toSet

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)

  val totalCount: N = Σ[N, Iterable[N]](tally.values)

  def observe(): A = ???

  def observe(gv: G): A = ???

  def probabilityOf(a: A): N = Σ[N, Iterable[N]](gvs.map(gv => tally((a, gv)))) / totalCount

  def probabilityOf(a: A, given: Case[G, N]): N = given match {
    case CaseIs(argGrv, gv)   => tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2))
    case CaseIsnt(argGrv, gv) => 1 - (tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2)))
    case _                    => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

