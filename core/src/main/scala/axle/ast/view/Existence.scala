
package axle.ast.view

import axle.ast._
import axle.Loggable

object Existence extends Loggable {

  def exists(stmt: Statement, node: MetaNode, grammar: Language): Boolean = stmt match {

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
      case MetaNodeRule(ruleName, mm, _) => {
        mm.contains(name) && {
          mm(name) match {
            case MetaNodeValue(None, _) => false
            case _ => true
          }
        }
      }
      case _ => throw new Exception("Can't apply Sub to a non-rule metanode")
    }

    case Attr(name) => node match {
      case MetaNodeRule(ruleName, mm, _) => {
        mm.contains(name) && {
          mm(name) match {
            case MetaNodeValue(None, _) => false
            case _ => true // need to recurse?
          }
        }
      }
      case _ => throw new Exception("Can't apply Attr to a non-rule metanode")
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
      case MetaNodeList(l: List[MetaNode], _) if n < l.length => {
        l(n) match {
          case MetaNodeValue(None, _) => false
          case _ => true // need to recurse?
        }
      }
      case _ => false
    }

  }

}
