package axle

import collection._
import math.log
import axle.Enrichments._
import axle.quanta.Information
import axle.Statistics._

object InformationTheory {

  def log2(x: Double) = log(x) / log(2)

  // Conditional probability table
  class CPT2[A, P1, P2](
    prior1: RandomVariable[P1],
    prior2: RandomVariable[P2],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) extends DistributionWithInput[A, (P1, P2)] {

    import Information._

    def getObjects() = values

    def entropy() = {
      val n = 1 // TODO
      n *: bit
    }

    def probabilityOf(a: A): Double = -1.0 // TODO

    def probabilityOf(a: A, given: Case[(P1, P2)]): Double = -1.0 // TODO

    def choose(): A = {
      // val pv1 = prior1.choose
      // val pv2 = prior2.choose
      null.asInstanceOf[A] // TODO
    }

  }

  def cpt[A, P1, P2](
    p1: RandomVariable[P1],
    p2: RandomVariable[P2],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) = new CPT2(p1, p2, values, m)

  def distribution[A](p: Map[A, Double]) = {

    // TODO: verify that the distribution sums to 1

    new DistributionNoInput[A] {

      import Information._

      override def getObjects() = p.keySet

      def X() = p.keySet

      // def randomStream(): Stream[Double] = Stream.cons(math.random, randomStream())

      // TODO Is there a version of scanLeft that is more like a reduce?
      // This would allow me to avoid having to construct the initial dummy element
      val bars = p.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1, x._2 + y._2))

      def choose(): A = {
        val r = math.random
        bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
      }

      def probabilityOf(a: A): Double = p(a)

      def entropy() = X.Σ(x => p(x) match {
        case 0.0 => 0.0
        case _ => -p(x) * log2(p(x))
      }) *: bit

    }
  }

  def coin(pHead: Double = 0.5) = distribution(Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))

}
