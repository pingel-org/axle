package axle

import collection._
import math.log
import axle.Enrichments._

import axle.quanta.Information

object InformationTheory {

  def log2(x: Double) = log(x) / log(2)

  trait Distribution[A, INPUT] {

    def getObjects(): Set[A]

    // val d: Map[A, Double]

    def choose(): A

    def entropy(): Information#UOM

    def H_:(): Information#UOM = entropy()
    
    def huffmanCode[S](alphabet: Set[S]): Map[A, Seq[S]] = {
      // TODO
      // http://en.wikipedia.org/wiki/Huffman_coding
      Map()
    }

  }

  // Conditional probability table
  class CPT2[A, P1, P2](
    prior1: RandomVariable[P1, _],
    prior2: RandomVariable[P2, _],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) extends Distribution[A, (P1, P2)] {

    import Information._

    def getObjects() = values

    def entropy() = {
      val n = 1 // TODO
      n *: bit
    }

    def choose(): A = {
      // val pv1 = prior1.choose
      // val pv2 = prior2.choose
      null.asInstanceOf[A] // TODO
    }

  }

  def cpt[A, P1, P2](
    p1: RandomVariable[P1, _],
    p2: RandomVariable[P2, _],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) = new CPT2(p1, p2, values, m)

  def distribution[A](p: Map[A, Double]) = {

    // TODO: verify that the distribution sums to 1

    new Distribution[A, Unit] {

      import Information._

      override def getObjects() = p.keySet

      def X() = p.keySet
      
      def choose(): A = {
        var r = math.random
        for ((k, v) <- p) {
          r -= v
          if (r < 0) {
            return k
          }
        }
        throw new Exception("malformed distribution")
      }

      def entropy() = X.Σ(x => -p(x) * log2(p(x))) *: bit

    }
  }

  def coin(pHead: Double = 0.5) = distribution(Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))

  trait Case {
    def ∧(right: Case) = CaseAnd(this, right)
    def ∨(right: Case) = CaseOr(this, right)
    def |(given: Case) = CaseGiven(this, given)
  }

  case class CaseAnd(left: Case, right: Case) extends Case {

  }

  case class CaseOr(left: Case, right: Case) extends Case {

  }

  case class CaseGiven(c: Case, given: Case) extends Case {
    // TODO: use phantom types to ensure that only one "given" clause is specified
  }

  case class CaseIs[A](v: A) extends Case {

  }

  case class CaseIsnt[A](v: A) extends Case {

  }

  case class RandomVariable[A, INPUT](name: String, distribution: Distribution[A, INPUT]) {
    def ==(v: A) = CaseIs(v)
    def !=(v: A) = CaseIsnt(v)
  }

  // Probability of
  case class P(c: Case) extends Function0[Double] {
    def *(right: () => Double) = PMultiply(this, right)
    def apply() = 1.0
  }

  case class PMultiply(left: P, right: () => Double) extends Function0[Double] {
    def apply() = 1.0
  }

  def bayes(p: P): () => Double = p.c match {
    // TODO: also check that "left" and "right" have no "given"
    case CaseAnd(left, right) => P(left | right) * bayes(P(right))
    case _ => P(p.c)
  }

}
