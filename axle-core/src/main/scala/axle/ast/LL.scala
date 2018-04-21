
package axle.ast

import cats.Show
import cats.kernel.Eq
import cats.implicits._

trait Symbol {
  def label: String
}

object Symbol {

  implicit val symbolEq = Eq.fromUniversalEquals[Symbol]

  implicit def showSymbol: Show[Symbol] = _.label
}

case class Terminal(label: String) extends Symbol

case class NonTerminal(label: String) extends Symbol

object NonTerminal {
  implicit val eqNT = Eq.fromUniversalEquals[NonTerminal]
}

object ⊥ extends Terminal("⊥") // also known as '$'

object ε extends Symbol {
  def label = "ε"
  // TODO terminal or non-terminal?
}

case class LLRule(id: Int, from: NonTerminal, rhs: List[Symbol])

object LLRule {
  implicit def showLLRule: Show[LLRule] =
    llr => llr.from.toString + " -> " + llr.rhs.mkString("", " ", "")
}

sealed trait LLParserAction
case class Shift() extends LLParserAction
case class Reduce(rule: LLRule) extends LLParserAction
case class ParseError(msg: String) extends LLParserAction

object LLParserAction {

  implicit def showLLParserAction: Show[LLParserAction] = _.toString

}

object LLParserState {

  implicit def showLLParserState: Show[LLParserState] =
    llps =>
      llps.inputBufferWithMarker + "\n" + llps.stack.mkString("", " ", "")

}

case class LLParserState(
  grammar: LLLanguage,
  input:   String,
  stack:   List[Symbol],
  // derivation: List[LLParserAction],
  i: Int) {

  lazy val inputBufferWithMarker = input.substring(0, i) + "|" + input.substring(i, input.length)

  def inputSymbol: Terminal = grammar.terminalsByName(input(i).show)

  def apply(action: LLParserAction): LLParserState = action match {
    case Shift() => {
      assert(stack.head == inputSymbol)
      LLParserState(grammar, input, stack.tail, i + 1)
    }
    case Reduce(rule) => {
      assert(stack.head == rule.from)
      LLParserState(grammar, input, rule.rhs ++ stack.tail, i)
    }
    case ParseError(msg) => { sys.error(this.show + "\nparse error: " + msg) }
  }

  def nextAction(): LLParserAction = stack.head match {

    case sts if sts == inputSymbol => Shift()

    case foo @ NonTerminal(_) =>
      if (grammar.parseTable.contains((foo, inputSymbol))) {
        Reduce(grammar.parseTable((foo, inputSymbol)))
      } else {
        ParseError("no rule")
      }

    case _ => ParseError("stack = " + stack + ", inputSymbol = " + inputSymbol + ". A non-matching non-terminal")

  }

  def finished: Boolean = input.length == i && stack.head == ⊥

}
