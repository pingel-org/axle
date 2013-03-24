
package axle.logic

object FOPL {

  trait Statement {

    def ∧(right: Statement) = And(this, right)
    def and(right: Statement) = And(this, right)

    def ∨(right: Statement) = Or(this, right)
    def or(right: Statement) = Or(this, right)

    def ⇔(right: Statement) = Iff(this, right)
    def iff(right: Statement) = Iff(this, right)

    def ⊃(right: Statement) = Implies(this, right)
    def implies(right: Statement) = Implies(this, right)
  }

  case class And(left: Statement, right: Statement) extends Statement {
    override def toString() = "(" + left + " ∧ " + right + ")"
  }
  case class Or(left: Statement, right: Statement) extends Statement {
    override def toString() = "(" + left + " ∨ " + right + ")"
  }
  case class Iff(left: Statement, right: Statement) extends Statement {
    override def toString() = "(" + left + " ⇔ " + right + ")"
  }
  case class Implies(left: Statement, right: Statement) extends Statement {
    override def toString() = "(" + left + " ⊃ " + right + ")"
  }

  case class ¬(statement: Statement) extends Statement
  case class ∃(symbol: Symbol, statement: Statement) extends Statement
  case class ∀(symbol: Symbol, statement: Statement) extends Statement

  def not(statement: Statement) = ¬(statement)
  def exists(symbol: Symbol, statement: Statement) = ∃(symbol, statement)
  def forall(symbol: Symbol, statement: Statement) = ∀(symbol, statement)

  case class Constant(b: Boolean) extends Statement {
    override def toString() = b.toString
  }

  abstract class Predicate(args: Symbol*) extends Statement {
    def apply(args: Symbol*): Predicate
    def map(f: Symbol => Symbol): Predicate = apply(args.map(f): _*)
  }

  implicit def foplBoolean(b: Boolean) = Constant(b)

  def skolemFor(i: Int, s: Symbol) = Symbol(s.name + i)

  def noOp(s: Statement): Statement = s match {
    case And(left, right) => And(noOp(left), noOp(right))
    case Or(left, right) => Or(noOp(left), noOp(right))
    case Iff(left, right) => Iff(noOp(left), noOp(right))
    case Implies(left, right) => Implies(noOp(left), noOp(right))
    case ¬(inner) => ¬(noOp(inner))
    case ∃(sym, e) => ∃(sym, noOp(e))
    case ∀(sym, e) => ∀(sym, noOp(e))
    case _ => s
  }

  def eliminateIff(s: Statement): Statement = s match {
    case And(left, right) => And(eliminateIff(left), eliminateIff(right))
    case Or(left, right) => Or(eliminateIff(left), eliminateIff(right))
    case Iff(left, right) => {
      val leftResult = eliminateIff(left)
      val rightResult = eliminateIff(right)
      (leftResult ⊃ rightResult) ∧ (rightResult ⊃ leftResult)
    }
    case Implies(left, right) => Implies(eliminateIff(left), eliminateIff(right))
    case ¬(inner) => ¬(eliminateIff(inner))
    case ∃(sym, e) => ∃(sym, eliminateIff(e))
    case ∀(sym, e) => ∀(sym, eliminateIff(e))
    case _ => s
  }

  def eliminateImplication(s: Statement): Statement = s match {
    case And(left, right) => And(eliminateImplication(left), eliminateImplication(right))
    case Or(left, right) => Or(eliminateImplication(left), eliminateImplication(right))
    case Iff(left, right) => ??? //Iff(eliminateImplication(left), eliminateImplication(right))
    case Implies(left, right) => ¬(eliminateImplication(left)) ∨ eliminateImplication(right)
    case ¬(inner) => ¬(eliminateImplication(inner))
    case ∃(sym, e) => ∃(sym, eliminateImplication(e))
    case ∀(sym, e) => ∀(sym, eliminateImplication(e))
    case _ => s
  }

  def moveNegation(s: Statement, incoming: Boolean = false): Statement = s match {

    case And(left, right) =>
      if (incoming)
        Or(moveNegation(left, true), moveNegation(right, true))
      else
        And(moveNegation(left), moveNegation(right))

    case Or(left, right) =>
      if (incoming)
        And(moveNegation(left, true), moveNegation(right, true))
      else
        Or(moveNegation(left), moveNegation(right))

    case Iff(left, right) => ??? // Iff(moveNegation(left), moveNegation(right))

    case Implies(left, right) => ??? //Implies(moveNegation(left), moveNegation(right))

    case ¬(inner) => if (incoming) moveNegation(inner) else moveNegation(inner, true)

    case ∃(sym, e) =>
      if (incoming)
        ∀(sym, moveNegation(e, true))
      else
        ∃(sym, moveNegation(e))

    case ∀(sym, e) =>
      if (incoming)
        ∃(sym, moveNegation(e, true))
      else
        ∀(sym, moveNegation(e))

    case _ => if (incoming) ¬(s) else s
  }

  // TODO: the skolem constants should actually be functions of the universally quantified vars
  // TODO: create a monadic context for skolem count

  def skolemize(s: Statement, m: Map[Symbol, Int] = Map()): Statement = s match {
    case And(left, right) => And(skolemize(left, m), skolemize(right, m))
    case Or(left, right) => Or(skolemize(left, m), skolemize(right, m))
    case Iff(left, right) => ??? // Iff(skolemize(left, m), skolemize(right, m))
    case Implies(left, right) => ??? // Implies(skolemize(left, m), skolemize(right, m))
    case ¬(inner) => ¬(skolemize(inner, m))
    case ∃(sym, e) => skolemize(e, m + (sym -> 1))
    case ∀(sym, e) => skolemize(e, m)
    case p: Predicate => p.map(s => if (m.contains(s)) skolemFor(1, s) else s) // TODO replace "1"
  }

  def distribute(s: Statement) = _distribute(s)._1

  def _distribute(s: Statement): (Statement, Boolean) = s match {
    case And(l, r) => {
      val (ld, lc) = _distribute(l)
      val (rd, rc) = _distribute(r)
      (And(ld, rd), lc || rc)
    }
    case Or(l, And(rl, rr)) => {
      val (ld, lc) = _distribute(l)
      val (rld, rlc) = _distribute(rl)
      val (rrd, rrc) = _distribute(rr)
      (And(Or(ld, rld), Or(ld, rrd)), true)
    }
    case Or(And(ll, lr), r) => {
      val (lld, llc) = _distribute(ll)
      val (rd, rc) = _distribute(r)
      val (lrd, lrc) = _distribute(lr)
      (And(Or(lld, rd), Or(lrd, rd)), true)
    }
    case Or(l, r) => {
      val (ld, lc) = _distribute(l)
      val (rd, rc) = _distribute(r)
      if (lc || rc) _distribute(Or(ld, rd)) else (Or(ld, rd), false)
    }
    case Iff(left, right) => ??? // Iff(distribute(left), distribute(right))
    case Implies(left, right) => ??? // Implies(distribute(left), distribute(right))
    case ¬(inner) => {
      val (id, ic) = _distribute(inner)
      (¬(id), ic)
    }
    case ∃(sym, e) => {
      val (ed, ec) = _distribute(e)
      (∃(sym, ed), ec)
    }
    case ∀(sym, e) => {
      val (ed, ec) = _distribute(e)
      (∀(sym, ed), ec)
    }
    case _ => (s, false)
  }

  def flatten(s: Statement): Statement = s match {
    case And(left, right) => And(flatten(left), flatten(right))
    case Or(left, right) => Or(flatten(left), flatten(right))
    case Iff(left, right) => ??? // Iff(flatten(left), flatten(right))
    case Implies(left, right) => ??? // Implies(flatten(left), flatten(right))
    case ¬(inner) => ¬(flatten(inner))
    case ∃(sym, e) => ∃(sym, flatten(e))
    case ∀(sym, e) => ∀(sym, flatten(e))
    case _ => s
  }

  def conjunctiveNormalForm(s: Statement): Statement =
    flatten(distribute(skolemize(moveNegation(eliminateImplication(eliminateIff(s))))))

  def disjunctList(s: Statement): List[Statement] = s match {
    case Or(head, tail) => head :: disjunctList(tail)
    case _ => List(s)
  }

  def disjoin(cs: List[Statement]): Statement = cs.reduceOption(Or(_, _)).getOrElse(false)

  def conjunctList(s: Statement): List[Statement] = s match {
    case And(head, tail) => head :: conjunctList(tail)
    case _ => List(s)
  }

  def conjoin(cs: List[Statement]): Statement = cs.reduceOption(And(_, _)).getOrElse(true)

  def atomicDisjunctsToImplication(s: Statement): Statement =
    disjunctList(s)
      .partition(a => a match { case ¬(_) => true case _ => false }) match {
        case (negatives, positives) =>
          conjoin(negatives.map({ case ¬(x) => x })) ⊃ disjoin(positives)
      }

  def implicativeNormalForm(s: Statement): List[Statement] =
    conjunctList(s).map(atomicDisjunctsToImplication(_))

  object SamplePredicates {

    case class A(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = A(args(0))
    }
    case class B(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = B(args(0))
    }
    case class C(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = C(args(0))
    }
    case class D(s: Symbol, t: Symbol) extends Predicate(s, t) {
      def apply(args: Symbol*) = D(args(0), args(1))
    }
    case class E(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = E(args(0))
    }
    case class F(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = F(args(0))
    }
    case class G(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = G(args(0))
    }
    case class H(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = H(args(0))
    }
    case class M(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = M(args(0))
    }
    case class N(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = N(args(0))
    }
    case class P(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = P(args(0))
    }
    case class Q(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = Q(args(0))
    }
    case class R(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = R(args(0))
    }
    case class S(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = S(args(0))
    }
    case class T(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = T(args(0))
    }
    case class U(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = U(args(0))
    }
    case class W(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = W(args(0))
    }
    case class X(s: Symbol) extends Predicate(s) {
      def apply(args: Symbol*) = X(args(0))
    }
    case class Y(s: Symbol, t: Symbol) extends Predicate(s, t) {
      def apply(args: Symbol*) = Y(args(0), args(1))
    }
    case class Z(s: Symbol, t: Symbol, u: Symbol) extends Predicate(s, t, u) {
      def apply(args: Symbol*) = Z(args(0), args(1), args(2))
    }

  }

}