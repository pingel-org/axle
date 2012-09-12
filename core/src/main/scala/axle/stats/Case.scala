package axle.stats

import collection._
import axle._

trait Case[A] {
  def and[B](right: Case[B]): Case[(A, B)] = CaseAnd[A, B](this, right)
  def ∧[B](right: Case[B]): Case[(A, B)] = CaseAnd[A, B](this, right)
  def ∩[B](right: Case[B]): Case[(A, B)] = CaseAnd[A, B](this, right)
  def or[B](right: Case[B]): Case[(A, B)] = CaseOr[A, B](this, right)
  def ∨[B](right: Case[B]): Case[(A, B)] = CaseOr[A, B](this, right)
  def ∪[B](right: Case[B]): Case[(A, B)] = CaseOr[A, B](this, right)
  def |[B](given: Case[B]): Case[(A, B)] = CaseGiven[A, B](this, given)
  def probability[B](given: Option[Case[B]] = None): Double
  def bayes(): () => Double // perhaps bayes should return a Seq[Case] or similar
}

case class CaseAndGT[A](conjuncts: GenTraversable[Case[A]]) extends Case[List[A]] {

  def probability[B](given: Option[Case[B]] = None): Double =
    given
      .map(g => conjuncts.Π((c: Case[A]) => () => P(c | g)()))
      .getOrElse(conjuncts.Π((c: Case[A]) => () => P(c)()))

  def bayes() = null // TODO
}

case class CaseAnd[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {

  def probability[C](given: Option[Case[C]] = None): Double =
    given
      .map(g => P(left | g)() * P(right | g)())
      .getOrElse(P(left)() * P(right)())

  def bayes() = P(left | right) * P(right).bayes() // TODO: also check that "left" and "right" have no "given"

}

case class CaseOr[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {

  def probability[C](given: Option[Case[C]] = None): Double =
    given
      .map(g => P(left | g)() + P(right | g)() - P((left ∧ right) | g)())
      .getOrElse(P(left)() + P(right)() - P(left ∧ right)())

  def bayes() = () => this.probability()

}

// TODO: use phantom types to ensure that only one "given" clause is specified
case class CaseGiven[A, B](c: Case[A], given: Case[B]) extends Case[(A, B)] {

  def probability[C](givenArg: Option[Case[C]] = None): Double = {
    assert(givenArg.isEmpty)
    c.probability(Some(given))
  }

  def bayes() = () => this.probability()

}

// TODO: may want CaseIs{With, No] classes to avoid the run-time type-checking below
case class CaseIs[A](rv: RandomVariable[A], v: A) extends Case[A] {

  def probability[B](given: Option[Case[B]]): Double = rv match {
    case rvNo: RandomVariable0[A] => rvNo.probability(v)
    case rvWith: RandomVariable1[A, B] => given
      .map(g => rvWith.probability(v, g))
      .getOrElse(rvWith.probability(v))
  }

  def bayes() = () => this.probability()

  override def toString(): String = rv.name + " = " + v
}

case class CaseIsnt[A](rv: RandomVariable[A], v: A) extends Case[A] {

  def probability[B](given: Option[Case[B]] = None): Double = 1.0 - P(rv eq v)()

  def bayes() = () => this.probability()

}
