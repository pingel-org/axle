package axle

object Statistics {

  import collection._
  import axle.Enrichments._
  import axle.quanta.Information

  trait Case[A] {
    def ∧[B](right: Case[B]): Case[(A, B)] = CaseAnd[A, B](this, right)
    def ∨[B](right: Case[B]): Case[(A, B)] = CaseOr[A, B](this, right)
    def |[B](given: Case[B]): Case[(A, B)] = CaseGiven[A, B](this, given)
    def probability[B](given: Option[Case[B]] = None): Double
  }

  case class CaseAndGT[A](conjuncts: GenTraversable[Case[A]]) extends Case[List[A]] {
    def probability[B](given: Option[Case[B]] = None): Double = conjuncts.Π((c: Case[A]) => () => c.probability(given))
  }

  case class CaseAnd[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {
    def probability[C](given: Option[Case[C]] = None): Double = left.probability(given) * right.probability(given)
  }

  case class CaseOr[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {
    def probability[C](given: Option[Case[C]] = None): Double = left.probability(given) + right.probability(given)
  }

  // TODO: use phantom types to ensure that only one "given" clause is specified
  case class CaseGiven[A, B](c: Case[A], given: Case[B]) extends Case[(A, B)] {
    def probability[C](givenArg: Option[Case[C]] = None): Double = {
      assert(givenArg.isEmpty)
      c.probability(Some(given))
    }
  }

  case class CaseIs[A](rv: RandomVariable[A], v: A) extends Case[A] {
    def probability[B](given: Option[Case[B]]): Double = rv.probability(v, given)
  }

  case class CaseIsnt[A](rv: RandomVariable[A], v: A) extends Case[A] {
    def probability[B](given: Option[Case[B]] = None): Double = 1 - rv.probability(v, given)
  }

  case class EnrichedCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) {
    def ∧(): Case[List[A]] = CaseAndGT(cgt)
  }

  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  case class RandomVariable[A](name: String, values: Option[Set[A]] = None, input: Option[Distribution[A, _]] = None) {
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability[B](a: A, given: Option[Case[B]]): Double = input.get.probabilityOf(a, given)

  }

  case class P[A](c: Case[A]) extends Function0[Double] {
    def *(right: Double) = PMultiply(this, () => right)
    def *(right: () => Double) = PMultiply(this, right)
    def apply(): Double = c.probability()
  }

  case class PMultiply[A](left: P[A], right: () => Double) extends Function0[Double] {
    def apply() = left() * right()
  }

  def bayes[A](p: P[A]): () => Double = p.c match {
    // TODO: also check that "left" and "right" have no "given"
    case CaseAnd(left, right) => P(left | right) * bayes(P(right))
    case _ => P(p.c)
  }

  trait Distribution[A, I] {

    def getObjects(): Set[A]

    // val d: Map[A, Double]

    def choose(): A

    def probabilityOf[B](a: A, given: Option[Case[B]]): Double

    def entropy(): Information#UOM

    def H_:(): Information#UOM = entropy()

    def huffmanCode[S](alphabet: Set[S]): Map[A, Seq[S]] = {
      // TODO
      // http://en.wikipedia.org/wiki/Huffman_coding
      Map()
    }

  }

  class TallyDistribution[A, G, I](name: String, tally: Map[(A, G), Int]) extends Distribution[A, I] {

    def getObjects(): Set[A] = null // TODO

    def choose(): A = null.asInstanceOf[A] // TODO

    def probabilityOf[B](a: A, given: Option[Case[B]]): Double = {
      0.0 // "a"
    }

    def entropy(): Information#UOM = null // TODO
  }

}