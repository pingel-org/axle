package axle.stats

import scala.util.Random

import axle.Σ
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Order
import spire.algebra.Ring
import spire.implicits.eqOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.orderOps

class TallyDistribution0[A, N: Field: Order](tally: Map[A, N])
  extends Distribution0[A, N] {

  val ring = implicitly[Ring[N]]
  val addition = implicitly[AdditiveMonoid[N]]

  def values: IndexedSeq[A] = tally.keys.toVector

  def map[B](f: A => B): Distribution0[B, N] =
    new TallyDistribution0(
      values
        .map({ v => f(v) -> probabilityOf(v) })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(addition.plus)))

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N] =
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

  val totalCount: N = Σ(tally.values)(identity)

  val bars: Map[A, N] =
    tally.scanLeft((null.asInstanceOf[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2)))

  def observe(): A = {
    val r: N = totalCount * Random.nextDouble()
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): N = tally.get(a).getOrElse(ring.zero) / totalCount
}

class TallyDistribution1[A, G: Eq, N: Field: Order](tally: Map[(A, G), N])
  extends Distribution1[A, G, N] {

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector

  def values: IndexedSeq[A] = _values

  val gvs = tally.keys.map(_._2).toSet

  val totalCount = Σ(tally.values)(identity)

  def observe(): A = ???

  def observe(gv: G): A = ???

  def probabilityOf(a: A): N = Σ(gvs.map(gv => tally((a, gv))))(identity) / totalCount

  def probabilityOf(a: A, given: Case[G, N]): N = given match {
    case CaseIs(argGrv, gv) => tally((a, gv)) / Σ(tally.filter(_._1._2 === gv).map(_._2))(identity)
    case CaseIsnt(argGrv, gv) => 1 - (tally((a, gv)) / Σ(tally.filter(_._1._2 === gv).map(_._2))(identity))
    case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

