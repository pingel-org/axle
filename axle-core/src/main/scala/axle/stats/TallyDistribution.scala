package axle.stats

import scala.util.Random

import spire.optional.unicode.Σ
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Order
import spire.algebra.Ring
import spire.compat.ordering
import spire.implicits.eqOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.orderOps

class TallyDistribution0[A, N: Field: Order](tally: Map[A, N], val name: String = "unnamed")
  extends Distribution0[A, N] {

  val ring = implicitly[Ring[N]]
  val addition = implicitly[AdditiveMonoid[N]]

  def values: IndexedSeq[A] = tally.keys.toVector

  def map[B](f: A => B): TallyDistribution0[B, N] =
    new TallyDistribution0(
      values
        .map({ v => f(v) -> probabilityOf(v) })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(addition.plus)))

  def flatMap[B](f: A => Distribution0[B, N]): TallyDistribution0[B, N] =
    new TallyDistribution0(
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

  val totalCount: N = Σ(tally.values)

  val bars: Map[A, N] =
    tally.scanLeft((null.asInstanceOf[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2)))

  val order = implicitly[Order[N]]

  def observe(): A = {
    val r: N = totalCount * Random.nextDouble()
    bars.find({ case (_, v) => order.gt(v, r) }).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): N = tally.get(a).getOrElse(ring.zero) / totalCount

  def show(implicit order: Order[A]): String =
    s"$name\n" +
      values.sorted.map(a => {
        val aString = a.toString
        (aString + (1 to (charWidth - aString.length)).map(i => " ").mkString("") + " " + probabilityOf(a).toString)
      }).mkString("\n")

}

class TallyDistribution1[A, G: Eq, N: Field: Order](tally: Map[(A, G), N], _name: String = "unnamed")
  extends Distribution1[A, G, N] {

  def name: String = _name

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector

  def values: IndexedSeq[A] = _values

  val gvs = tally.keys.map(_._2).toSet

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)

  val totalCount = Σ(tally.values)

  def observe(): A = ???

  def observe(gv: G): A = ???

  def probabilityOf(a: A): N = Σ(gvs.map(gv => tally((a, gv)))) / totalCount

  def probabilityOf(a: A, given: Case[G, N]): N = given match {
    case CaseIs(argGrv, gv) => tally((a, gv)) / Σ(tally.filter(_._1._2 === gv).map(_._2))
    case CaseIsnt(argGrv, gv) => 1 - (tally((a, gv)) / Σ(tally.filter(_._1._2 === gv).map(_._2)))
    case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

