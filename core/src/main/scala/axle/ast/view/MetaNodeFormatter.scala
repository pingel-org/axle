
package axle.ast.view

import axle.ast._
import axle.Loggable
import util.matching.Regex
import collection._

import xml._

trait Accumulator {

  def accRaw(s: String) = {}

  def accNewline() = {}

  def accSpace() = {}

  def accSpaces() = {}

  def accSpan(spanclass: String, s: String) = {}

  def accPushStack() = {}

  def accPopAndWrapStack(label: String) = {}

}

abstract class MetaNodeFormatter[R, S](language: Language, highlight: Set[MetaNode], conform: Boolean)
  extends Accumulator {
  def result: R
  var tokens: S

  var indentation_level: Int = 0
  var column: Int = 0
  var needs_indent: Boolean = true
  var lineno = 1
  var stack = new mutable.Stack[Option[(Int, String)]]()
  var _node2lineno = mutable.Map[MetaNode, Int]()

  def isConforming() = conform
  def shouldHighlight(node: MetaNode) = highlight.contains(node)

  def node2lineno = _node2lineno

  def _indent(): Unit = {
    if (column == 0 && needs_indent) {
      // val indentation = ( for ( x <- 1 to indentation_level) yield tab ).mkString("")
      (1 to indentation_level).map(x => accSpaces()) // result.append(<span>&nbsp;&nbsp;&nbsp;</span>)
      column = 3 * indentation_level // indentation.length()
    }
    needs_indent = false
  }

  def needsParens(attr_name: String, node: MetaNode, subtree: MetaNode, grammar: Language): Boolean =
    (node, subtree) match {

      case (MetaNodeRule(nr, nm, _), MetaNodeRule(sr, sm, _)) => {
        grammar.lowerThan(sr, nr) match {
          case true => true
          case false => {
            val node_rule = grammar.name2rule(nr)
            val subtree_rule = grammar.name2rule(sr)
            attr_name match {
              // TODO assumptions about the names of "left" and "right" should be externalized
              case "left" => subtree_rule.precedenceLevel == node_rule.precedenceLevel && node_rule.associativity == "right" // (2 ** 3) ** 4
              case "right" => subtree_rule.precedenceLevel == node_rule.precedenceLevel && node_rule.associativity == "left"
              case _ => false
            }
          }
        }
      }
      case (_, _) => false
    }

  def raw(element: String): Unit = {
    if (element != null) {
      _indent()
      val lines = element.split("\n")
      lines.size match {
        case 0 | 1 => {
          column += element.length()
        }
        case _ => {
          lineno += lines.size - 1
          column = lines.last.length()
        }
      }
      // result.append(Text(element))
      accRaw(element)
    }
  }

  def wrap(indent: Boolean = false): Unit = {
    // TODO !!! toka.append("\\")
    newline(true, null, indent)
  }

  def space(): Unit = {
    _indent()
    if (column > 80) {
      wrap()
    } else {
      column += 1
    }
    // result.append(Text(" "))
    accSpace()
  }

  def indent: Unit = indentation_level += 1

  def dedent: Unit = indentation_level -= 1

  def beginSpan(): Unit = accPushStack

  def endSpan(spanType: String): Unit = accPopAndWrapStack(spanType)

  def conformTo(node: MetaNode): Unit = {

  }

  def newline(hard: Boolean, node: MetaNode, indent: Boolean = true): Unit = {

    // println("MetaNodeFormatter.newline(hard="+hard+", node="+node+", indent="+indent+")")
    // println("   column = " + column)
    // println("   conform = " + conform)

    // && node.getLineNo.isDefined && ( node.getLineNo.get < lineno )
    if ((node != null) && conform) {
      if (column > 0) {
        column = 0
        needs_indent = indent
        lineno += 1
        // result.appendAll(<br></br>)
        accNewline()
      }
    } else if (hard || column > 0) {
      column = 0
      needs_indent = indent
      lineno += 1
      // result.appendAll(<br></br>)
      accNewline()
    }
  }

  def keyword(kw: String): Unit = {
    _indent()
    column += kw.length()
    // result.appendAll(<span class={"keyword"}>{kw}</span>)
    accSpan("keyword", kw)
  }

  def operator(op: String): Unit = {
    _indent()
    column += op.length()
    // result.appendAll(<span class={"operator"}>{op}</span>)
    accSpan("operator", op)
  }

  def repr(r: String): Unit = r match {
    case null => {}
    case _ => {
      _indent()
      column += r.length()
      // result.appendAll(<span class={"repr"}>{scala.xml.Utility.escape(r)}</span>)
      accSpan("repr", xml.Utility.escape(r))
      // NOTE: may not need to escape non-html
    }
  }

  def name(n: String): Unit = n match {
    case null => {}
    case _ => {
      _indent()
      val special = false // TODO
      special match {
        case true => {
          val (s, span) = (n, "special") // TODO
          column += s.length
          accSpan(span, s)
        }
        case false => {
          column += n.length()
          // result.append(Text(n))
          accRaw(n)
        }
      }

    }
  }

  def enterFor(): Unit = stack.push(None)

  def updateFor(varName: String): Unit =
    stack.push(
      Some(
        stack
          .pop()
          .map(frame => (frame._1 + 1, varName))
          .getOrElse((0, varName))
      )
    )

  def leaveFor(): Unit = stack.pop()

  // def result(): String = toka.toString()

}
