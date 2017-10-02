package axle.stats

/**
 * TODO
 * 
 * Define Case expressions to support full range of case types
 *
 */

case class CaseIs[A](value: A, variable: Variable[A], is: Boolean = true) {

  def and[R](right: CaseIs[R]) = CaseAnd(this, right)
  def ∧[R](right: CaseIs[R]) = CaseAnd(this, right)
  def ∩[R](right: CaseIs[R]) = CaseAnd(this, right)

  def or[R](right: CaseIs[R]) = CaseOr(this, right)
  def ∨[R](right: CaseIs[R]) = CaseOr(this, right)
  def ∪[R](right: CaseIs[R]) = CaseOr(this, right)

  def |[R](given: CaseIs[R]) = CaseGiven(this, given)
}
case class CaseAnd[L, R](left: CaseIs[L], right: CaseIs[R])
case class CaseOr[L, R](left: CaseIs[L], right: CaseIs[R])
case class CaseGiven[A, G](c: CaseIs[A], given: CaseIs[G])
case class CaseGiven2[A, G1, G2](c: CaseIs[A], given: CaseAnd[G1, G2])

object CaseIs {

  import cats.Show

  implicit def showCaseIs[A]: Show[CaseIs[A]] =
    new Show[CaseIs[A]] {
      def show(c: CaseIs[A]): String = {
        import c._
        val opStr = if(c.is) " = " else " != "
        variable.name + opStr + value
      }
    }
}
