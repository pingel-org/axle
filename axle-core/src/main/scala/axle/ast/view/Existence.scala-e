
package axle.ast.view

import axle.ast.Affix
import axle.ast.Arglist
import axle.ast.AstNode
import axle.ast.AstNodeList
import axle.ast.AstNodeRule
import axle.ast.AstNodeValue
import axle.ast.Attr
import axle.ast.CR
import axle.ast.CRH
import axle.ast.Dedent
import axle.ast.Emb
import axle.ast.For
import axle.ast.ForDel
import axle.ast.Indent
import axle.ast.J
import axle.ast.JItems
import axle.ast.Kw
import axle.ast.Language
import axle.ast.Lit
import axle.ast.Nop
import axle.ast.Op
import axle.ast.PosKw
import axle.ast.Repr
import axle.ast.Sp
import axle.ast.Spread
import axle.ast.Sq
import axle.ast.SqT
import axle.ast.Statement
import axle.ast.Sub
import axle.ast.Var
import axle.ast.VarN

object Existence {

  def exists(stmt: Statement, node: AstNode, grammar: Language): Boolean = stmt match {

    case Spread() => true

    case Nop() => true

    case Lit(value: String) => true

    case Repr(name) => true

    case Emb(left, stmt, right) => exists(stmt, node, grammar)

    case Kw(value) => true

    case PosKw(values) => true

    case Sp() => true

    case Op(value) => true

    case Indent() => true

    case Dedent() => true

    case CR() => true

    case CRH() => true

    case Arglist() => true

    case Sq(stmts @ _*) => true

    case SqT(stmts @ _*) => stmts.find(!exists(_, node, grammar)).isEmpty

    case Sub(name) => node match {
      case AstNodeRule(ruleName, mm, _) => {
        mm.contains(name) && {
          mm(name) match {
            case AstNodeValue(None, _) => false
            case _ => true
          }
        }
      }
      case _ => throw new Exception("Can't apply Sub to a non-rule AstNode")
    }

    case Attr(name) => node match {
      case AstNodeRule(ruleName, mm, _) => {
        mm.contains(name) && {
          mm(name) match {
            case AstNodeValue(None, _) => false
            case _ => true // need to recurse?
          }
        }
      }
      case _ => throw new Exception("Can't apply Attr to a non-rule AstNode")
    }

    case For(subtree, stmt) => {
      true // TODO !!!
    }

    case ForDel(subtree, stmt, delimiter) => {
      //info("exists ForDel?:")
      //info("stmt   : " + stmt)
      //info("node   : " + node)
      true // TODO !!!
    }

    case J(subtree, stmt) => true // TODO !!!

    case JItems(subtree, inner, outer) => true // TODO !!!

    case Affix(subtree, prefix, postfix) => true // TODO !!!

    case Var() => true // TODO ?

    case VarN(n) => node match {
      case AstNodeList(l: List[AstNode], _) if n < l.length => {
        l(n) match {
          case AstNodeValue(None, _) => false
          case _ => true // need to recurse?
        }
      }
      case _ => false
    }

  }

}
