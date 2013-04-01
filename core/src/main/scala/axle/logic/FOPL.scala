
package axle.logic

object FOPL {

  abstract class Predicate(symbols: Symbol*) extends Function1[Map[Symbol, Any], Boolean] with Statement {
    def symbolSet() = symbols.toSet
    def name(): String
    override def toString(): String = name() + "(" + symbols.mkString(", ") + ")"
    // def apply(args: Symbol*): Predicate
    // def map(f: Symbol => Symbol): Predicate = apply(args.map(f): _*)
  }

  trait Statement {

    def apply(symbolTable: Map[Symbol, Any]): Boolean

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
    def apply(symbolTable: Map[Symbol, Any]): Boolean = left(symbolTable) && right(symbolTable)
    override def toString() = "(" + left + " ∧ " + right + ")"
  }
  case class Or(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = left(symbolTable) || right(symbolTable)
    override def toString() = "(" + left + " ∨ " + right + ")"
  }
  case class Iff(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = {
      val lv = left(symbolTable)
      val rv = right(symbolTable)
      (!lv && !rv) || (lv && rv)
    }
    override def toString() = "(" + left + " ⇔ " + right + ")"
  }
  case class Implies(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = (!left(symbolTable)) || right(symbolTable)
    override def toString() = "(" + left + " ⊃ " + right + ")"
  }

  case class ¬(statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = !statement(symbolTable)
  }

  case class ElementOf[T](symbol: Symbol, set: Set[T])

  class EnrichedSymbol(symbol: Symbol) {
    def in[T](set: Set[T]) = ElementOf(symbol, set)
    def ∈[T](set: Set[T]) = ElementOf(symbol, set)
  }

  implicit val enrichSymbol = (symbol: Symbol) => new EnrichedSymbol(symbol)

  case class ∃[T](symbolSet: ElementOf[T], statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean =
      symbolSet.set.exists(v => statement(symbolTable + (symbolSet.symbol -> v)))
  }
  case class ∀[T](symbolSet: ElementOf[T], statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean =
      symbolSet.set.forall(v => statement(symbolTable + (symbolSet.symbol -> v)))
  }

  def not(statement: Statement) = ¬(statement)
  def exists[T](symbolSet: ElementOf[T], statement: Statement) = ∃(symbolSet, statement)
  def forall[T](symbolSet: ElementOf[T], statement: Statement) = ∀(symbolSet, statement)

  case class Constant(b: Boolean) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = b
    override def toString() = b.toString
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

  def freeVariables(s: Statement, notFree: Set[Symbol] = Set()): Set[Symbol] = s match {
    case And(left, right) => freeVariables(left, notFree).union(freeVariables(right, notFree))
    case Or(left, right) => freeVariables(left, notFree).union(freeVariables(right, notFree))
    case Iff(left, right) => freeVariables(left, notFree).union(freeVariables(right, notFree))
    case Implies(left, right) => freeVariables(left, notFree).union(freeVariables(right, notFree))
    case ¬(inner) => freeVariables(inner, notFree)
    case ∃(symbolSet, e) => freeVariables(e, notFree + symbolSet.symbol)
    case ∀(symbolSet, e) => freeVariables(e, notFree + symbolSet.symbol)
    case pred: Predicate => pred.symbolSet -- notFree
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

    case ∃(symbolSet, e) =>
      if (incoming)
        ∀(symbolSet, moveNegation(e, true))
      else
        ∃(symbolSet, moveNegation(e))

    case ∀(symbolSet, e) =>
      if (incoming)
        ∃(symbolSet, moveNegation(e, true))
      else
        ∀(symbolSet, moveNegation(e))

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
    case ∃(symbolSet, e) => skolemize(e, m) //  + (sym -> 1)
    case ∀(symbolSet, e) => skolemize(e, m)
    case p: Predicate => p // .map(s => if (m.contains(s)) skolemFor(1, s) else s) // TODO replace "1"
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

  def _flatten(s: Statement): (Statement, Boolean) = s match {

    case And(And(ll, lr), r) =>
      (And(_flatten(ll)._1, _flatten(And(_flatten(lr)._1, _flatten(r)._1))._1), true)

    case And(left, right) => {
      val (lf, lc) = _flatten(left)
      val (rf, rc) = _flatten(right)
      (And(lf, rf), lc || rc)
    }

    case Or(Or(ll, lr), r) =>
      (Or(_flatten(ll)._1, _flatten(Or(_flatten(lr)._1, _flatten(r)._1))._1), true)

    case Or(left, right) => {
      val (lf, lc) = _flatten(left)
      val (rf, rc) = _flatten(right)
      (Or(lf, rf), lc || rc)
    }

    case Iff(left, right) => ???
    case Implies(left, right) => ???

    case ¬(inner) => {
      // Note: only makes sense when inner is an atom
      val (innerFlat, innerChanged) = _flatten(inner)
      (¬(innerFlat), innerChanged)
    }

    case ∃(sym, e) => ???
    case ∀(sym, e) => ???
    case _ => (s, false)
  }

  def flatten(s: Statement): Statement = _flatten(s)._1

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

}