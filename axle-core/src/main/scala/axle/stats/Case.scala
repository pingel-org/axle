package axle.stats

import scala.collection.GenTraversable

import spire.optional.unicode.Π
import spire.algebra.Field
import spire.implicits.additiveGroupOps

trait Case[A, N] {

  implicit def field: Field[N]

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

case class CaseAndGT[A: Manifest, N](conjuncts: Iterable[Case[A, N]])(implicit val field: Field[N])
  extends Case[List[A], N] {

  def probability[B](given: Option[Case[B, N]] = None): N =
    given
      .map(g => Π(conjuncts map { (c: Case[A, N]) => P(c | g).apply() }))
      .getOrElse(Π(conjuncts map { P(_).apply() }))

  def bayes = ???
}

case class CaseAnd[A, B, N](left: Case[A, N], right: Case[B, N])(implicit val field: Field[N])
  extends Case[(A, B), N] {

  def probability[C](given: Option[Case[C, N]] = None): N =
    (given.map(g => P(left | g) * P(right | g)).getOrElse(P(left) * P(right))).apply()

  def bayes = {
    // TODO: also check that "left" and "right" have no "given"
    P(left | right) * P(right).bayes
  }

}

case class CaseOr[A, B, N](left: Case[A, N], right: Case[B, N])(implicit val field: Field[N])
  extends Case[(A, B), N] {

  def probability[C](given: Option[Case[C, N]] = None): N =
    given
      .map { g =>
        // P(left | g) + P(right | g) - P((left ∧ right) | g)
        field.plus(P(left | g).apply(), P(right | g).apply()) - P((left ∧ right) | g).apply()
      }
      .getOrElse(
        field.plus(P(left).apply(), P(right).apply()) - P(left ∧ right).apply())

  def bayes = () => this.probability()

}

// TODO: use phantom types to ensure that only one "given" clause is specified
case class CaseGiven[A, B, N](c: Case[A, N], given: Case[B, N])(implicit val field: Field[N])
  extends Case[(A, B), N] {

  def probability[C](givenArg: Option[Case[C, N]] = None): N = {
    assert(givenArg.isEmpty)
    c.probability(Some(given))
  }

  def bayes = () => this.probability()

}

// TODO: may want CaseIs{With, No] classes to avoid the run-time type-checking below
case class CaseIs[A, N](distribution: Distribution[A, N], v: A)(implicit val field: Field[N])
  extends Case[A, N] {

  def probability[G](given: Option[Case[G, N]]): N =
    distribution match {
      case d0: Distribution0[A, N] => d0.probabilityOf(v)
      case d1: Distribution1[A, G, N] => given
        .map(g => d1.probabilityOf(v, g))
        .getOrElse(d1.probabilityOf(v))
    }

  def bayes = () => this.probability()

}

object CaseIs {

  import axle.Show

  implicit def showCaseIs[A, N]: Show[CaseIs[A, N]] = new Show[CaseIs[A, N]] {

    def text(c: CaseIs[A, N]): String = {
      import c._
      distribution.name + " = " + v
    }
  }

}

case class CaseIsnt[A, N](distribution: Distribution[A, N], v: A)(implicit val field: Field[N])
  extends Case[A, N] {

  def probability[B](given: Option[Case[B, N]] = None): N = field.minus(field.one, P(distribution is v).apply())

  def bayes = () => this.probability()

}
