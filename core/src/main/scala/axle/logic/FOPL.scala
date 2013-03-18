
object FOPL {

  trait Statement {

    def ∧(right: Statement) = And(this, right)
    def ∨(right: Statement) = Or(this, right)
    def ⇔(right: Statement) = Iff(this, right)
    def ⊃(right: Statement) = Implies(this, right)

  }

  case class And(left: Statement, right: Statement) extends Statement
  case class Or(left: Statement, right: Statement) extends Statement
  case class Iff(left: Statement, right: Statement) extends Statement
  case class Implies(left: Statement, right: Statement) extends Statement

  case class ¬(e: Statement) extends Statement
  case class ∃(s: Symbol, e: Statement) extends Statement
  case class ∀(s: Symbol, e: Statement) extends Statement

  case class Constant(b: Boolean) extends Statement

  implicit def foplBoolean(b: Boolean) = Constant(b)

  def skolemFor(i: Int, s: Symbol): Symbol = ???
  
  def eliminateIff(s: Statement): Statement = ???

  def eliminateImplication(s: Statement): Statement = ???

  def moveNegation(s: Statement): Statement = ???

  def skolemize(s: Statement): Statement = ???

  def distribute(s: Statement): Statement = ???

  def flatten(s: Statement): Statement = ???

  def conjunctiveNormalForm(s: Statement): Statement = ???

  def implicativeNormalForm(s: Statement): List[Statement] = ???

}