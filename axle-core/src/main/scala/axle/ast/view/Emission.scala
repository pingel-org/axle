package axle.ast.view

import axle.ast._
import spire.implicits._
import scala.collection.mutable.WrappedArray

object Emission {

  def emit[R, S](
    stmt: Statement,
    nodeOpt: Option[AstNode],
    grammar: Language,
    formatter: AstNodeFormatter[R, S]): AstNodeFormatter[R, S] = {

    (nodeOpt, stmt) match {

      case (Some(node @ AstNodeRule(_, m, _)), Sub(name)) => {
        val subtree = m(name)
        if (formatter.needsParens(name, node, subtree, grammar)) {
          emit(grammar, subtree, formatter.raw("(")).raw(")")
        } else {
          emit(grammar, subtree, formatter)
        }
      }

      case (Some(AstNodeRule(_, m, _)), Spread()) => {
        m("spread") match {
          case AstNodeList(l, _) => l.foldLeft(formatter)({
            case (f, c) =>
              emit(grammar, c, f).newline(false, Some(c))
          })
          case _ => throw new Exception("spread statement is applied to something other than AstNodeList")
        }
      }

      case (_, Nop()) => formatter.raw("") // TODO is there an empty Node?

      case (Some(AstNodeRule(_, m, _)), Attr(attr)) =>
        m(attr).asInstanceOf[AstNodeValue].value.foldLeft(formatter)({ case (f, v) => f.name(v) })

      case (_, Lit(value: String)) => formatter.raw(value)

      case (_, Sq(stmts @ _*)) =>
        stmts.foldLeft(formatter)({ case (f, s) => emit(s, nodeOpt, grammar, f) })

      // stmts.filter( ! Existence.exists(_, node, grammar) ).map( s => formatter.raw("") )
      case (Some(node), SqT(stmts @ _*)) =>
        if (stmts.forall(Existence.exists(_, node, grammar))) {
          stmts.foldLeft(formatter)({ case (f, s) => emit(s, nodeOpt, grammar, f) })
        } else {
          formatter
        }

      // TODO !!! replace toString() with the equiv of repr()
      case (Some(AstNodeRule(_, m, _)), Repr(name)) =>
        m(name).asInstanceOf[AstNodeValue].value.map(formatter.repr).getOrElse(formatter)

      case (_, Emb(left, stmt, right)) =>
        emit(stmt, nodeOpt, grammar, formatter.raw(left)).raw(right)

      case (_, Kw(value)) => formatter.keyword(value)

      case (_, PosKw(initial, rest)) => formatter.state.stack.head.map(_ match {
        case (0, _) => formatter.keyword(initial)
        case _ => formatter.keyword(rest)
      }).getOrElse(formatter)

      case (_, Sp()) => formatter.space

      case (_, Op(value)) => formatter.operator(value)

      case (Some(AstNodeRule(_, m, _)), For(subtree, body)) => {
        val elems = m(subtree).asInstanceOf[AstNodeList]
        ((0 until elems.list.length).foldLeft(formatter.enterFor)({
          case (f, i) =>
            val c = elems.list(i)
            emit(body, Some(c), grammar, f.updateFor("TODO c"))
        })).leaveFor
      }

      case (Some(AstNodeRule(_, m, _)), ForDel(subtree, body, delimiter)) => {
        val elems = m(subtree).asInstanceOf[AstNodeList]
        ((0 until elems.list.length).foldLeft(formatter.enterFor)({
          case (f, i) =>
            val c = elems.list(i)
            val f2 = emit(body, Some(c), grammar, f.updateFor("TODO c"))
            if (i < elems.list.length - 1) {
              f2.raw(delimiter)
            } else {
              f2
            }
        })).leaveFor
      }

      case (Some(AstNodeRule(_, m, _)), J(subtree, delimiter)) => {
        val elems = m(subtree).asInstanceOf[AstNodeList]
        val n = elems.list.length - 1
        (0 to n).foldLeft(formatter)({
          case (f, i) => {
            val f1 = emit(grammar, elems.list(i), f)
            if (i < n) {
              emit(delimiter, None, grammar, f1)
            } else {
              f1
            }
          }
        })
      }

      // TODO? python set elems = node.items
      case (Some(AstNodeRule(_, m, _)), JItems(subtree, inner, outer)) => {
        val elems = m(subtree).asInstanceOf[AstNodeList]
        (0 until elems.list.length).foldLeft(formatter)({
          case (f, i) => {
            val l = elems.list(i).asInstanceOf[AstNodeList].list
            val fN = emit(grammar, l(1), emit(grammar, l(0), f).raw(inner))
            if (i < elems.list.length - 1) {
              fN.raw(outer)
            } else {
              fN
            }
          }
        })
      }

      case (Some(AstNodeRule(_, m, _)), Affix(subtree, prefix, postfix)) =>
        m(subtree).asInstanceOf[AstNodeList].list.foldLeft(formatter)({
          case (f, c) => {
            val f = emit(grammar, c, formatter.raw(prefix))
            postfix.map(f.raw).getOrElse(f)
          }
        })

      case (_, Indent()) => formatter.indent

      case (_, Dedent()) => formatter.dedent

      case (_, CR()) => formatter.newline(false, nodeOpt)

      case (_, CRH()) => formatter.newline(true, nodeOpt)

      case (Some(node), Var()) => emit(grammar, node, formatter)

      case (Some(AstNodeList(l, _)), VarN(n)) => emit(grammar, l(n), formatter)

      case (Some(AstNodeRule(_, m, _)), Arglist()) => {
        // Note: This is far too python-specific
        val argnames = m("argnames").asInstanceOf[AstNodeList]
        val defaults = m("defaults").asInstanceOf[AstNodeList]
        val arity = argnames.list.length
        val num_defaults = defaults.list.length
        val num_undefaulted = arity - num_defaults
        (0 until argnames.list.length).foldLeft(formatter)({
          case (f, i) => {
            val flags = m("flags").asInstanceOf[AstNodeValue].value
            val f1 = if ((((flags.get === "4") && i === arity - 1) || ((flags.get === "12") && i == arity - 2))) {
              f.raw("*")
            } else if ((flags.get === "8" || flags.get === "12") && i === arity - 1) {
              f.raw("**")
            } else {
              f
            }
            val f2 = emit(grammar, argnames.list(i), f1)
            val f3 = if (i >= num_undefaulted) {
              emit(grammar, defaults.list(i - num_undefaulted), f2.raw("="))
            } else {
              f2
            }
            if (i < arity - 1) {
              f3.raw(",").space
            } else {
              f3
            }
          }
        })
      }
    }

  }

  def emit[R, S](
    grammar: Language,
    node: AstNode,
    formatter: AstNodeFormatter[R, S]): AstNodeFormatter[R, S] = {

    val fLn = formatter.markLine(node, node.lineNo)

    node match {

      case AstNodeValue(v, _) => v.map(fLn.raw).getOrElse(fLn)

      case AstNodeList(l, _) => (0 until l.length)
        .foldLeft(fLn)({
          case (f, i) =>
            val f1 = emit(grammar, l(i), f)
            if (i < (l.length - 1)) {
              f1.space
            } else {
              f1
            }
        })

      case AstNodeRule(r, m, lineno) => {
        val f = formatter.conformTo(node)
        if (f.shouldHighlight(node)) {
          emit(grammar.name2rule(r).statement, Some(node), grammar, f.beginSpan).endSpan("highlight")
        } else {
          emit(grammar.name2rule(r).statement, Some(node), grammar, f)
        }
      }
    }
  }

}
