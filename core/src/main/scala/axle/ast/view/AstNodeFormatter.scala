
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

abstract class AstNodeFormatter[R, S](language: Language, highlight: Set[AstNode], conform: Boolean)
  extends Accumulator {

  def result(): R
  val tokens: S

  var indentationLevel = 0
  var column = 0
  var needsIndent = true
  var lineno = 1
  val stack = new mutable.Stack[Option[(Int, String)]]()
  val _node2lineno = mutable.Map[AstNode, Int]()

  def isConforming() = conform
  def shouldHighlight(node: AstNode) = highlight.contains(node)

  def node2lineno = _node2lineno

  def _indent(): Unit = {
    if (column == 0 && needsIndent) {
      // val indentation = ( for ( x <- 1 to indentationLevel) yield tab ).mkString("")
      (1 to indentationLevel).map(x => accSpaces()) // result.append(<span>&nbsp;&nbsp;&nbsp;</span>)
      column = 3 * indentationLevel // indentation.length()
    }
    needsIndent = false
  }

  def needsParens(attr_name: String, node: AstNode, subtree: AstNode, grammar: Language): Boolean =
    (node, subtree) match {
      case (AstNodeRule(nodeRuleName, nm, _), AstNodeRule(subtreeRuleName, sm, _)) => {
        val nodeRule = grammar.name2rule(nodeRuleName)
        val subtreeRule = grammar.name2rule(subtreeRuleName)
        grammar.lowerThan(subtreeRule, nodeRule) match {
          case Some(true) => true
          case Some(false) => attr_name match {
            // TODO assumptions about the names of "left" and "right" should be externalized
            case "left" => grammar.precedenceOf(subtreeRule) == grammar.precedenceOf(nodeRule) && grammar.associativityOf(nodeRule) == "right" // (2 ** 3) ** 4
            case "right" => grammar.precedenceOf(subtreeRule) == grammar.precedenceOf(nodeRule) && grammar.associativityOf(nodeRule) == "left"
            case _ => false
          }
          case None => true // is this right?
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

  def indent: Unit = indentationLevel += 1

  def dedent: Unit = indentationLevel -= 1

  def beginSpan(): Unit = accPushStack

  def endSpan(spanType: String): Unit = accPopAndWrapStack(spanType)

  def conformTo(node: AstNode): Unit = {

  }

  def newline(hard: Boolean, node: AstNode, indent: Boolean = true): Unit = {

    // println("AstNodeFormatter.newline(hard="+hard+", node="+node+", indent="+indent+")")
    // println("   column = " + column)
    // println("   conform = " + conform)

    // && node.getLineNo.isDefined && ( node.getLineNo.get < lineno )
    if ((node != null) && conform) {
      if (column > 0) {
        column = 0
        needsIndent = indent
        lineno += 1
        // result.appendAll(<br></br>)
        accNewline()
      }
    } else if (hard || column > 0) {
      column = 0
      needsIndent = indent
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
