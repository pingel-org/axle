package axle.stats

import axle.Σ
import scala.util.Random
import spire.algebra.Eq
import spire.implicits._
import spire.syntax._
import spire.math.Rational
import spire.algebra.Order
import spire.algebra.Ring
import spire.algebra.AdditiveMonoid
import spire.random.mutable.Cmwc5
import spire.algebra.Field
import spire.compat.ordering

class TallyDistribution0[A: Order, N: Field: Order](tally: Map[A, N])
  extends Distribution0[A, N] {

  val ring = implicitly[Ring[N]]
  val addition = implicitly[AdditiveMonoid[N]]

  def values: IndexedSeq[A] = tally.keys.toVector.sorted

//  def map[B](f: A => B): Distribution[B, N] = 42
//
//  def flatMap[B](f: A => Distribution[B, N]): Distribution[B, N] = 42

  val totalCount: N = Σ(tally.values)(identity)

  val bars: Map[A, N] =
    tally.scanLeft((null.asInstanceOf[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2)))

  def observe(): A = {
    val r: N = totalCount * Random.nextDouble()
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): N = tally.get(a).getOrElse(ring.zero) / totalCount
}

class TallyDistribution1[A: Order, G: Eq, N: Field: Order](tally: Map[(A, G), N])
  extends Distribution1[A, G, N] {

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector.sortWith(implicitly[Order[A]].lt)

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

