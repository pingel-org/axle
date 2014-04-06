package axle.stats

import collection.GenTraversable
import axle._
import spire.math._
import spire.implicits._
import spire.algebra._

abstract class Case[A, N: Field] {
  
  def and[B](right: Case[B, N]): Case[(A, B), N] = CaseAnd(this, right)
  def ∧[B](right: Case[B, N]): Case[(A, B), N] = CaseAnd(this, right)
  def ∩[B](right: Case[B, N]): Case[(A, B), N] = CaseAnd(this, right)
  def or[B](right: Case[B, N]): Case[(A, B), N] = CaseOr(this, right)
  def ∨[B](right: Case[B, N]): Case[(A, B), N] = CaseOr(this, right)
  def ∪[B](right: Case[B, N]): Case[(A, B), N] = CaseOr(this, right)
  def |[B](given: Case[B, N]): Case[(A, B), N] = CaseGiven(this, given)
  def probability[B](given: Option[Case[B, N]] = None): N
  def bayes: () => N // perhaps bayes should return a Seq[Case] or similar
}

case class CaseAndGT[A: Manifest, N: Field](conjuncts: GenTraversable[Case[A, N]])
  extends Case[List[A], N] {

  def probability[B](given: Option[Case[B, N]] = None): N =
    given
      .map(g => Π(conjuncts)({ (c: Case[A, N]) => P(c | g).apply() }))
      .getOrElse(Π(conjuncts)({ P(_) }))

  def bayes = ???
}

case class CaseAnd[A, B, N: Field](left: Case[A, N], right: Case[B, N])
  extends Case[(A, B), N] {
  
  def probability[C](given: Option[Case[C, N]] = None): N =
    (given.map(g => P(left | g) * P(right | g)).getOrElse(P(left) * P(right))).apply()

  def bayes = {
    // TODO: also check that "left" and "right" have no "given"
    P(left | right) * P(right).bayes
  }

}

case class CaseOr[A, B, N: Field](left: Case[A, N], right: Case[B, N])
  extends Case[(A, B), N] {

  val field = implicitly[Field[N]]

  def probability[C](given: Option[Case[C, N]] = None): N =
    given
      .map { g =>
        // P(left | g) + P(right | g) - P((left ∧ right) | g)
        field.plus(P(left | g), P(right | g)) - P((left ∧ right) | g)
      }
      .getOrElse(
        field.plus(P(left), P(right)) - P(left ∧ right))

  def bayes = () => this.probability()

}

// TODO: use phantom types to ensure that only one "given" clause is specified
case class CaseGiven[A, B, N: Field](c: Case[A, N], given: Case[B, N])
  extends Case[(A, B), N] {

  def probability[C](givenArg: Option[Case[C, N]] = None): N = {
    assert(givenArg.isEmpty)
    c.probability(Some(given))
  }

  def bayes = () => this.probability()

}

// TODO: may want CaseIs{With, No] classes to avoid the run-time type-checking below
case class CaseIs[A, N: Field](rv: RandomVariable[A, N], v: A)
  extends Case[A, N] {

  def probability[B](given: Option[Case[B, N]]): N = rv match {
    case rvNo: RandomVariable0[A, N] => rvNo.probability(v)
    case rvWith: RandomVariable1[A, B, N] => given
      .map(g => rvWith.probability(v, g))
      .getOrElse(rvWith.probability(v))
  }

  def bayes = () => this.probability()

  override def toString: String = rv.name + " = " + v
}

case class CaseIsnt[A, N: Field](rv: RandomVariable[A, N], v: A)
  extends Case[A, N] {

  val field = implicitly[Field[N]]

  def probability[B](given: Option[Case[B, N]] = None): N = field.minus(field.one, P(rv is v))

  def bayes = () => this.probability()

}
