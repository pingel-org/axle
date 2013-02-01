
package axle.ast

/**
 *
 * http://en.wikipedia.org/wiki/LL_parser
 * http://www.jambe.co.nz/UNI/FirstAndFollowSets.html
 * http://www.cs.pitt.edu/~mock/cs1622/lectures/lecture10.pdf
 * see http://blog.rafaelferreira.net/2008/07/type-safe-builder-pattern-in-scala.html
 *
 * TODO test first and follow (including scribd example)
 * TODO model input buffer
 * TODO create AST
 */

import collection._
import axle.Loggable

class Symbol(_label: String) {
  def label() = _label
  //override def toString() = "'" + label + "'"
  override def toString() = _label
}

case class Terminal(_label: String) extends Symbol(_label)
case class NonTerminal(_label: String) extends Symbol(_label)
object Start extends NonTerminal("S")
object ⊥ extends Terminal("⊥") // maybe best left as '$'
object ε extends Symbol("ε") // TODO terminal or non-terminal?

case class LLRule(id: Int, from: NonTerminal, rhs: List[Symbol]) {
  override def toString() = from + " " + "->" + " " + rhs.mkString("", " ", "")
}

abstract class ParserAction()
object Shift extends ParserAction()
case class Reduce(rule: LLRule) extends ParserAction()
case class ParseError(msg: String) extends ParserAction()

class Parse(grammar: LLLanguage, symbol: String, input: String) {

  val inputBuffer = ""
  val stack = new mutable.Stack[Symbol]()

  var i = 0

  stack.push(⊥)
  stack.push(grammar.nonTerminalsByName(symbol))

  val derivation = new mutable.ListBuffer[ParserAction]()

  def inputBufferWithMarker = input.substring(0, i) + "|" + input.substring(i, input.length)

  override def toString =
    inputBufferWithMarker + "\n" +
      stack.reverse.mkString("", " ", "") + "\n"
  // + derivation.mkString("", ", ", "") + "\n"

  def inputSymbol: Terminal = grammar.terminalsByName(input(i).toString)

  def apply(action: ParserAction): Unit = {
    action match {
      case Shift => {
        assert(stack.top == inputSymbol)
        stack.pop
        i += 1
      }
      case Reduce(rule) => {
        assert(stack.top == rule.from)
        stack.pop
        stack.pushAll(rule.rhs.reverse)
      }
      case ParseError(msg) => { sys.error(this + "\nparse error: " + msg) }
    }
    derivation += action
  }

  def nextAction(): ParserAction = stack.top match {

    case sts if sts == inputSymbol => Shift

    case foo @ NonTerminal(_) =>
      if (grammar.parseTable.contains((foo, inputSymbol))) {
        Reduce(grammar.parseTable((foo, inputSymbol)))
      } else {
        ParseError("no rule")
      }

    case _ => ParseError("stack = " + stack + ", inputSymbol = " + inputSymbol + ". A non-matching non-terminal")

  }

  def done(): Boolean = if (input.length == i) {
    stack.top == ⊥
  } else {
    if (derivation.isEmpty) {
      false
    } else {
      derivation.head match {
        case ParseError(_) => true
        case _ => false
      }
    }
  }

  def parse() = while (!done) { apply(nextAction) }

}
