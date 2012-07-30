package axle.stats

import util.Random

class TallyDistribution0[A](tally: Map[A, Int])
  extends Distribution0[A] {

  val totalCount = tally.values.sum

  val bars = tally.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1, x._2 + y._2))

  def choose(): A = {
    val r = Random.nextInt(totalCount + 1)
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): Double = tally(a).toDouble / totalCount
}

class TallyDistribution1[A, G](tally: Map[(A, G), Int])
  extends Distribution1[A, G] {

  val gvs = tally.keys.map(k => k._2).toSet

  val totalCount = tally.values.sum

  def choose(): A = null.asInstanceOf[A] // TODO

  def choose(gv: G): A = null.asInstanceOf[A] // TODO

  def probabilityOf(a: A): Double = gvs.map(gv => tally((a, gv))).sum / totalCount

  def probabilityOf(a: A, given: Case[G]): Double = given match {
    case CaseIs(argGrv, gv) => tally((a, gv)).toDouble / tally.filter(_._1._2 == gv).map(_._2).sum
    case CaseIsnt(argGrv, gv) => 1.0 - (tally((a, gv)).toDouble / tally.filter(_._1._2 == gv).map(_._2).sum)
    case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
  }

}

