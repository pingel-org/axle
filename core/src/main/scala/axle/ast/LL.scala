
package axle.ast

// http://en.wikipedia.org/wiki/LL_parser
// http://www.jambe.co.nz/UNI/FirstAndFollowSets.html
// http://www.cs.pitt.edu/~mock/cs1622/lectures/lecture10.pdf
// see http://blog.rafaelferreira.net/2008/07/type-safe-builder-pattern-in-scala.html

// TODO test first and follow (including scribd example)
// TODO model input buffer
// TODO create lift webapp
// TODO create AST

import scala.collection._
import axle.Loggable

case class Symbol(label: String) {
  //override def toString() = "'" + label + "'"
  override def toString() = label
}
case class Terminal(override val label: String) extends Symbol(label)
case class NonTerminal(override val label: String) extends Symbol(label)
object Start extends NonTerminal("S")
object BottomOfStack extends Terminal("$")
object Epsilon extends Symbol("\u03B5") // TODO terminal or non-terminal?

case class LLRule(id: String, from: NonTerminal, rhs: List[Symbol]) {
  override def toString() = id + ": " + from + " " + "->" + " " + rhs.mkString("", " ", "")
}

abstract class ParserAction()
object Shift extends ParserAction()
case class Reduce(rule: LLRule) extends ParserAction()
case class ParseError(msg: String) extends ParserAction()

class Parse(grammar: LLLanguage, symbol: String, input: String) {

  var inputBuffer: String = ""
  var stack = new mutable.Stack[Symbol]()

  var i = 0

  stack.push(BottomOfStack)
  val target = grammar.nonTerminals(symbol)
  stack.push(target)

  var derivation = List[ParserAction]()

  def inputBufferWithMarker = input.substring(0, i) + "|" + input.substring(i, input.length)

  override def toString =
    inputBufferWithMarker + "\n" +
      stack.reverse.mkString("", " ", "") + "\n"
  // + derivation.mkString("", ", ", "") + "\n"

  def inputSymbol: Terminal = grammar.terminals(input(i).toString)

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
    derivation = action :: derivation
  }

  def nextAction: ParserAction = stack.top match {

    case sts if sts == inputSymbol => Shift

    case foo @ NonTerminal(_) => {
      if (grammar.parseTable.contains((foo, inputSymbol))) {
        Reduce(grammar.parseTable((foo, inputSymbol)))
      } else {
        ParseError("no rule")
      }
    }

    case _ => ParseError("stack = " + stack + ", inputSymbol = " + inputSymbol + ". A non-matching non-terminal")

  }

  def done: Boolean = (input.length == i) match {
    case true => stack.top match {
      case BottomOfStack => true
      case _ => false
    }
    case false => derivation.isEmpty match {
      case true => false
      case _ => derivation.head match {
        case ParseError(_) => true
        case _ => false
      }
    }
  }

  def parse = while (!done) { apply(nextAction) }

}

class ParseTableGrammarBuilder(ptgName: String) {

  var grammar = new LLLanguage(ptgName)

  def nt(label: String): ParseTableGrammarBuilder = {
    grammar.nonTerminals += (label -> NonTerminal(label))
    this
  }

  def t(label: String): ParseTableGrammarBuilder = {
    grammar.terminals += (label -> Terminal(label))
    this
  }

  def r(ruleId: String, from: String, x: String): ParseTableGrammarBuilder = {
    grammar.llRules += ruleId -> LLRule(ruleId,
      grammar.nonTerminals(from),
      List(grammar.getSymbol(x)).flatMap({ x => x })
    )
    this
  }

  def r(ruleId: String, from: String, strRhs: List[String]): ParseTableGrammarBuilder = {
    grammar.llRules += ruleId -> LLRule(ruleId,
      grammar.nonTerminals(from),
      strRhs.map(grammar.getSymbol(_)).flatMap({ x => x })
    )
    this
  }

  def build(): LLLanguage = {
    grammar.buildParseTable
    grammar
  }

}
