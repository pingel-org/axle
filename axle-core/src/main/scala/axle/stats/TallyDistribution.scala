package axle.stats

import axle._
import util.Random
import spire.math._
import spire.implicits._

class TallyDistribution0[A](tally: Map[A, Long])
  extends Distribution0[A] {

  val totalCount = tally.values.sum

  val bars: Map[A, Real] = tally.scanLeft((null.asInstanceOf[A], Real(0)))((x, y) => (y._1, x._2 + y._2))

  def observe(): A = {
    val r = (Random.nextDouble() * totalCount).toLong
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): Real = Rational(tally(a), totalCount)
}

class TallyDistribution1[A, G](tally: Map[(A, G), Long])
  extends Distribution1[A, G] {

  val gvs = tally.keys.map(k => k._2).toSet

  val totalCount = tally.values.sum

  def observe(): A = ???

  def observe(gv: G): A = ???

  def probabilityOf(a: A): Real = Rational(gvs.map(gv => tally((a, gv))).sum, totalCount)

  def probabilityOf(a: A, given: Case[G]): Real = given match {
    case CaseIs(argGrv, gv) => Rational(tally((a, gv)), tally.filter(_._1._2 == gv).map(_._2).sum)
    case CaseIsnt(argGrv, gv) => Real(1) - Rational(tally((a, gv)), tally.filter(_._1._2 == gv).map(_._2).sum)
    case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

