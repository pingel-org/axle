
package axle.logic

import spire.algebra.Eq
import spire.implicits.BooleanStructure
import spire.implicits.IntAlgebra
import spire.implicits.SeqEq
import spire.implicits.StringOrder
import spire.implicits.eqOps
import axle.Show
import axle.string

object FOPL {

  def skolemFor(skolems: Map[Symbol, Set[Symbol]], s: Symbol, universally: Set[Symbol]) = {
    val newSym = Symbol("sk" + skolems.size)
    (newSym, skolems + (newSym -> universally))
  }

  implicit def symbolEq: Eq[Symbol] = new Eq[Symbol] {
    def eqv(x: Symbol, y: Symbol): Boolean = x.equals(y)
  }

  object Predicate {

    implicit def predicateEq = new Eq[Predicate] {
      def eqv(x: Predicate, y: Predicate): Boolean =
        (x.name === y.name) && (x.symbols === y.symbols)
    }

    implicit def showPredicate: Show[Predicate] = new Show[Predicate] {

      def text(predicate: Predicate): String = {
        import predicate._
        name + "(" + symbols.mkString(", ") + ")"
      }
    }
  }

  abstract class Predicate(val symbols: Symbol*) extends Function1[Map[Symbol, Any], Boolean] with Statement {

    outer =>

    def symbolSet: Set[Symbol] = symbols.toSet

    def name: String

    def skolemize(universally: Set[Symbol], existentially: Set[Symbol], skolems: Map[Symbol, Set[Symbol]]): (Predicate, Map[Symbol, Set[Symbol]]) = {

      val symbolsSkolems = symbols.scanLeft((null.asInstanceOf[Symbol], skolems))({
        case (previous, s) =>
          if (existentially.contains(s)) skolemFor(previous._2, s, universally)
          else (s, previous._2)
      })

      val newPredicate = new Predicate(symbolsSkolems.tail.map(_._1): _*) {
        def name: String = outer.name
        def apply(symbolTable: Map[Symbol, Any]): Boolean = outer.apply(symbolTable)
      }

      (newPredicate, symbolsSkolems.last._2)
    }
  }

  object Statement {

    implicit def statementEq: Eq[Statement] = new Eq[Statement] {
      // TODO: How can I avoid this pattern match ?
      def eqv(x: Statement, y: Statement): Boolean = (x, y) match {
        case (l: Predicate, r: Predicate) => l === r
        case (l @ And(_, _), r @ And(_, _)) => l === r
        case (l @ Or(_, _), r @ Or(_, _)) => l === r
        case (l @ Iff(_, _), r @ Iff(_, _)) => l === r
        case (l @ Implies(_, _), r @ Implies(_, _)) => l === r
        case (l @ ¬(_), r @ ¬(_)) => l === r
        case (l @ ∃(_, _), r @ ∃(_, _)) => l === r
        case (l @ ∀(_, _), r @ ∀(_, _)) => l === r
        case (l @ Constant(_), r @ Constant(_)) => l === r
        case _ => false
      }
    }

    implicit def showStatement: Show[Statement] = new Show[Statement] {
      // TODO: How can I avoid this pattern match ?
      def text(s: Statement): String = s match {
        case p: Predicate => string(p)
        case a: And => string(a)
        case o: Or => string(o)
        case i: Iff => string(i)
        case i: Implies => string(i)
        case n @ ¬(_) => string(n)
        case e @ ∃(_, _) => string(e)
        case a @ ∀(_, _) => string(a)
        case c @ Constant(_) => string(c)
      }
    }

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

  object And {
    implicit def eqAnd: Eq[And] = new Eq[And] {
      def eqv(x: And, y: And): Boolean = (x.left === y.left && x.right === y.right)
    }
    implicit def showAnd: Show[And] = new Show[And] {
      def text(a: And): String = "(" + string(a.left) + " ∧ " + string(a.right) + ")"
    }
  }
  case class And(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = left(symbolTable) && right(symbolTable)
  }

  object Or {
    implicit def eqOr: Eq[Or] = new Eq[Or] {
      def eqv(x: Or, y: Or): Boolean = (x.left === y.left && x.right === y.right)
    }
    implicit def showOr: Show[Or] = new Show[Or] {
      def text(o: Or): String = "(" + string(o.left) + " ∨ " + string(o.right) + ")"
    }
  }
  case class Or(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = left(symbolTable) || right(symbolTable)
  }

  object Iff {
    implicit def eqIff: Eq[Iff] = new Eq[Iff] {
      def eqv(x: Iff, y: Iff): Boolean = (x.left === y.left && x.right === y.right)
    }
    implicit def showIff: Show[Iff] = new Show[Iff] {
      def text(i: Iff): String = "(" + string(i.left) + " ⇔ " + string(i.right) + ")"
    }
  }
  case class Iff(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = {
      val lv = left(symbolTable)
      val rv = right(symbolTable)
      (!lv && !rv) || (lv && rv)
    }
  }

  object Implies {
    implicit def eqImplies: Eq[Implies] = new Eq[Implies] {
      def eqv(x: Implies, y: Implies): Boolean = (x.left === y.left && x.right === y.right)
    }
    implicit def showImplies: Show[Implies] = new Show[Implies] {
      def text(i: Implies): String = "(" + string(i.left) + " ⊃ " + string(i.right) + ")"
    }
  }
  case class Implies(left: Statement, right: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = (!left(symbolTable)) || right(symbolTable)
  }

  object ¬ {
    implicit def eqNeg: Eq[¬] = new Eq[¬] {
      def eqv(x: ¬, y: ¬): Boolean = (x.statement === y.statement)
    }
    implicit def showNeg: Show[¬] = new Show[¬] {
      def text(n: ¬): String = "¬" + string(n.statement)
    }
  }
  case class ¬(statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = !statement(symbolTable)
  }

  object ElementOf {
    implicit def eqEO: Eq[ElementOf] = new Eq[ElementOf] {
      def eqv(x: ElementOf, y: ElementOf): Boolean =
        (x.symbol === y.symbol) && (x.set.intersect(y.set).size === x.set.size)
    }
    implicit def showEO: Show[ElementOf] = new Show[ElementOf] {
      def text(eo: ElementOf): String = eo.symbol + " ∈ " + eo.set
    }
  }
  case class ElementOf(symbol: Symbol, set: Set[Any])

  class EnrichedSymbol(symbol: Symbol) {
    def in(set: Set[Any]) = ElementOf(symbol, set)
    def ∈(set: Set[Any]) = ElementOf(symbol, set)
  }

  implicit val enrichSymbol = (symbol: Symbol) => new EnrichedSymbol(symbol)

  object ∃ {
    implicit def eqExists: Eq[∃] = new Eq[∃] {
      def eqv(x: ∃, y: ∃): Boolean =
        (x.symbolSet === y.symbolSet) && (x.statement === y.statement)
    }
    implicit def showExists: Show[∃] = new Show[∃] {
      def text(e: ∃): String = "∃" + e.symbolSet + " " + string(e.statement)
    }
  }
  case class ∃(symbolSet: ElementOf, statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean =
      symbolSet.set.exists(v => statement(symbolTable + (symbolSet.symbol -> v)))
  }

  object ∀ {
    implicit def eqAll: Eq[∀] = new Eq[∀] {
      def eqv(x: ∀, y: ∀): Boolean =
        (x.symbolSet === y.symbolSet) && (x.statement === y.statement)
    }
    implicit def showForall: Show[∀] = new Show[∀] {
      def text(a: ∀): String = "∀" + a.symbolSet + " " + string(a.statement)
    }
  }
  case class ∀(symbolSet: ElementOf, statement: Statement) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean =
      symbolSet.set.forall(v => statement(symbolTable + (symbolSet.symbol -> v)))
  }

  def not(statement: Statement) = ¬(statement)
  def exists(symbolSet: ElementOf, statement: Statement) = ∃(symbolSet, statement)
  def forall(symbolSet: ElementOf, statement: Statement) = ∀(symbolSet, statement)

  object Constant {
    implicit def eqConstant: Eq[Constant] = new Eq[Constant] {
      def eqv(x: Constant, y: Constant): Boolean =
        x.b === y.b
    }
    implicit def showConstant: Show[Constant] = new Show[Constant] {
      def text(c: Constant): String = c.toString
    }
  }
  case class Constant(b: Boolean) extends Statement {
    def apply(symbolTable: Map[Symbol, Any]): Boolean = b
  }

  implicit def foplBoolean(b: Boolean) = Constant(b)

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

  def _skolemize(s: Statement, universally: Set[Symbol], existentially: Set[Symbol], skolems: Map[Symbol, Set[Symbol]]): (Statement, Map[Symbol, Set[Symbol]]) = s match {
    case And(left, right) => {
      val (leftSkolemized, leftSkolems) = _skolemize(left, universally, existentially, skolems)
      val (rightSkolemized, rightSkolems) = _skolemize(right, universally, existentially, leftSkolems)
      (And(leftSkolemized, rightSkolemized), rightSkolems)
    }
    case Or(left, right) => {
      val (leftSkolemized, leftSkolems) = _skolemize(left, universally, existentially, skolems)
      val (rightSkolemized, rightSkolems) = _skolemize(right, universally, existentially, leftSkolems)
      (Or(leftSkolemized, rightSkolemized), rightSkolems)
    }
    case ∃(symbolSet, e) => _skolemize(e, universally, existentially + symbolSet.symbol, skolems)
    case ∀(symbolSet, e) => _skolemize(e, universally + symbolSet.symbol, existentially, skolems)
    case ¬(p: Predicate) => {
      val (innerSkolemized, innerSkolems) = p.skolemize(universally, existentially, skolems)
      (¬(innerSkolemized), innerSkolems)
    }
    case p: Predicate => p.skolemize(universally, existentially, skolems)
    case _ => ???
  }

  def skolemize(s: Statement): (Statement, Map[Symbol, Set[Symbol]]) = _skolemize(s, Set(), Set(), Map())

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

  def conjunctiveNormalForm(s: Statement): (Statement, Map[Symbol, Set[Symbol]]) = {
    val (skolemized, skolems) = skolemize(moveNegation(eliminateImplication(eliminateIff(s))))
    (flatten(distribute(skolemized)), skolems)
  }

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
