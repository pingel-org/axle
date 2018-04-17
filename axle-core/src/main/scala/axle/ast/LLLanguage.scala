
package axle.ast

import scala.Stream.cons
import scala.Stream.empty
import cats.Show
import cats.implicits._

/**
 *
 * http://www.scribd.com/doc/7185137/First-and-Follow-Set
 *
 */

object LLLanguage {

  implicit def showLLLanguage: Show[LLLanguage] = lll => view.ViewString.llLanguage(lll)
}

case class LLLanguage(
  name:                String,
  _llRuleDescriptions: List[(String, List[String])],
  startSymbolString:   String                       = "S") extends Language {

  def rules = Nil

  def precedenceGroups = Nil

  def parser = (text: String) => None

  def trimmer = ast => ast

  val nonTerminals: List[NonTerminal] = (_llRuleDescriptions.map(desc => NonTerminal(desc._1)).toSet).toList

  val nonTerminalsByName = nonTerminals.map(nt => (nt.label, nt)).toMap

  val startSymbol = nonTerminalsByName(startSymbolString)

  val terminals: List[Terminal] = (_llRuleDescriptions.flatMap(_._2).toSet -- _llRuleDescriptions.map(_._1)).map(Terminal(_)).toList.sortBy(_.label) ++ List(⊥)

  val terminalsByName = terminals.map(t => (t.label, t)).toMap

  val _llRules: List[LLRule] =
    _llRuleDescriptions.zipWithIndex
      .map({ case (desc, i) => LLRule(i + 1, nonTerminalsByName(desc._1), desc._2.map(symbol(_).get)) })

  def llRules: List[LLRule] = _llRules

  def symbol(label: String): Option[Symbol] =
    (if (terminalsByName.contains(label)) terminalsByName else nonTerminalsByName).get(label)

  /**
   *
   * 4. First(Y1Y2..Yk) is either
   *   1. First(Y1) (if First(Y1) doesn't contain epsilon)
   *   2. OR (if First(Y1) does contain epsilon) then First (Y1Y2..Yk) is everything in First(Y1) <except for epsilon > as well as everything in First(Y2..Yk)
   *   3. If First(Y1) First(Y2)..First(Yk) all contain epsilon then add epsilon to First(Y1Y2..Yk) as well.
   *
   */

  def first(XS: List[Symbol]): Set[Symbol] = XS match {
    case Nil => Set()
    case head :: rest => {
      val result = first(head)
      if (!result.contains(ε)) {
        result
      } else {
        rest match {
          case Nil => result
          case _   => first(rest) ++ Set(ε)
        }
      }
    }
  }

  /**
   *
   * 1. If X is a terminal then First(X) is just X
   * 2. If there is a Production X -> epsilon then add epsilon to first(X)
   * 3. If there is a Production X -> Y1Y2..Yk then add first(Y1Y2..Yk) to first(X)
   *
   */

  def first(X: Symbol): Set[Symbol] = X match {
    case Terminal(_) => Set(X)
    case nt @ NonTerminal(_) => {
      llRules.filter(_.from === nt).flatMap({ rule =>
        rule.rhs match {
          case List(ε) => Set(ε) // Case 2
          case _       => first(rule.rhs) // Case 3
        }
      }).toSet
    }
  }

  /**
   *
   * 1. First put ⊥ (the end of input marker) in Follow(S) (S is the start symbol)
   * 2. If there is a production A -> aBb, (where 'a' can be a whole string) then everything in FIRST(b) except for epsilon is placed in FOLLOW(B).
   * 3. If there is a production A -> aB, then everything in FOLLOW(A) is in FOLLOW(B)
   * 4. If there is a production A -> aBb, where FIRST(b) contains epsilon, then everything in FOLLOW(A) is in FOLLOW(B)
   *
   */

  def follow(symbol: NonTerminal, followMemo: Map[Symbol, Set[Symbol]]): (Set[Symbol], Map[Symbol, Set[Symbol]]) = {

    // import NonTerminal._

    if (followMemo.contains(symbol)) {
      (followMemo(symbol), followMemo)
    } else {
      // TODO allow Terminal(_) in cases below to be a list of Terminals
      val s0: Set[Symbol] = if (symbol === startSymbol) Set[Symbol](⊥) else Set()
      val result = llRules.foldLeft((s0, followMemo))({
        case (v: (Set[Symbol], Map[Symbol, Set[Symbol]]), rule: LLRule) =>
          {
            val (accSet, followMemo): (Set[Symbol], Map[Symbol, Set[Symbol]]) = v
            val x: Set[Symbol] = (rule.rhs match {
              // TODO?: enforce that rest is composed of only terminals (maybe not the case)
              case Terminal(_) :: symbol :: rest => first(rest).filter(x => !(x == ε))
              case _                             => Set()
            })
            val y: (Set[Symbol], Map[Symbol, Set[Symbol]]) = (rule.rhs match {
              case Terminal(_) :: symbol :: Nil => follow(rule.from, followMemo)
              case Terminal(_) :: symbol :: rest if (first(rest).contains(ε)) => follow(rule.from, followMemo)
              case _ => (Set(), followMemo)
            })
            (accSet ++ x ++ y._1, y._2)
          }
      })
      (result._1, result._2 + (symbol -> result._1))
    }
  }

  lazy val _parseTable: Map[(NonTerminal, Symbol), LLRule] = {
    nonTerminals foreach { nt =>
      first(nt) // TODO: where is this written?
    }
    llRules.flatMap({ rule =>
      first(rule.rhs).flatMap(a => {
        if (terminalsByName.contains(a.label)) {
          List((rule.from, a) -> rule)
        } else if (a == ε) {
          val memo0 = Map[Symbol, Set[Symbol]]() // TODO !!!
          val (foll, memo1) = follow(rule.from, memo0) // TODO including $
          foll.map(t => (rule.from, t) -> rule)
        } else {
          Nil
        }
      })
    }).toMap
  }

  def parseTable: Map[(NonTerminal, Symbol), LLRule] = _parseTable

  def parseStateStream(state: LLParserState): Stream[(LLParserAction, LLParserState)] =
    if (state.finished) {
      empty
    } else {
      val action = state.nextAction
      action match {
        case ParseError(x) => empty
        case _ => {
          val nextState = state(action)
          cons((action, nextState), parseStateStream(nextState))
        }
      }
    }

  def startState(input: String): LLParserState = LLParserState(this, input, List(startSymbol, ⊥), 0)

  def parseDebug(input: String): String =
    parseStateStream(startState(input)).toList
      .map({
        case (action, state) =>
          action.show + "\n" +
            "  " + state.inputBufferWithMarker + "\n" +
            "  " + state.stack.mkString("")
      }).mkString("\n\n")

  def parse(input: String): Option[List[LLRule]] = {
    val record = parseStateStream(startState(input)).toList
    if (record.last._2.finished) {
      Some(record.map(_._1).flatMap({
        case Reduce(rule) => List(rule)
        case _            => Nil
      }))
    } else {
      None
    }
  }

}
