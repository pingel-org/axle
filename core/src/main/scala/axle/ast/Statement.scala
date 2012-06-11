
package axle.ast

abstract class Statement

case class Sub(name: String) extends Statement

case class Spread() extends Statement

case class Nop() extends Statement

case class Attr(name: String) extends Statement

case class Lit(value: String) extends Statement

case class Sq(stmts: Statement*) extends Statement

case class SqT(stmts: Statement*) extends Statement // like Seq, but test=True

case class Repr(name: String) extends Statement

case class Emb(left: String, stmt: Statement, right: String) extends Statement

case class Kw(value: String) extends Statement

case class PosKw(vals: String*) extends Statement

case class Sp() extends Statement

case class Op(value: String) extends Statement

case class For(subtree: String, stmt: Statement) extends Statement

case class ForDel(subtree: String, stmt: Statement, delimiter: String) extends Statement

case class J(subtree: String, stmt: Statement) extends Statement

case class JItems(subtree: String, inner: String, outer: String) extends Statement

case class Affix(subtree: String, prefix: String, postfix: Option[String]) extends Statement

case class Indent() extends Statement

case class Dedent() extends Statement

case class CR() extends Statement

case class CRH() extends Statement // like CR, but hard=True

case class Var() extends Statement

case class VarN(n: Int) extends Statement

case class Arglist() extends Statement // Too python-specifi
