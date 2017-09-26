package axle.stats

sealed trait CaseExpr {

  def and[R](right: R) = CaseAnd(this, right)
  def ∧[R](right: R) = CaseAnd(this, right)
  def ∩[R](right: R) = CaseAnd(this, right)

  def or[R](right: R) = CaseOr(this, right)
  def ∨[R](right: R) = CaseOr(this, right)
  def ∪[R](right: R) = CaseOr(this, right)

  def |[R](given: R) = CaseGiven(this, given)
}

/**
 * TODO
 * 1) use phantom types to ensure that only one "given" clause is specified for CaseGiven
 * 2) handle other arity distributions with CaseIs and CaseIsnt
 *
 */

case class CaseAndGT[A: Manifest](conjuncts: Iterable[A]) extends CaseExpr
case class CaseAnd[L, R](left: L, right: R) extends CaseExpr
case class CaseOr[L, R](left: L, right: R) extends CaseExpr
case class CaseGiven[A, B](c: A, given: B) extends CaseExpr
case class CaseIs[A](v: A, rv: Variable[A]) extends CaseExpr
case class CaseIsnt[A](v: A, rv: Variable[A]) extends CaseExpr

object CaseIs {

  import cats.Show

  implicit def showCaseIs[A]: Show[CaseIs[A]] =
    new Show[CaseIs[A]] {
      def show(c: CaseIs[A]): String = {
        import c._
        rv.name + " = " + v
      }
    }
}
