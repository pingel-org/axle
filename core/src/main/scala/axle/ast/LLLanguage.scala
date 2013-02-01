
package axle.ast

import axle._
import util.matching.Regex
import collection._

object LLLanguage {

  // implicit def tuple2llRule(tuple2: (String, List[String])) = LLRule(tuple2._1, tuple2._2)

}

case class LLLanguage(
  name: String,
  _llRuleDescriptions: List[(String, List[String])]) extends Language(
  name, Nil, Nil, (text: String) => None, ast => ast
) {

  val _nonTerminals = _llRuleDescriptions.map(desc => NonTerminal(desc._1)).toSet + Start

  def nonTerminals = _nonTerminals

  val nonTerminalsByName = _nonTerminals.map(nt => (nt.label, nt)).toMap

  val _terminals = (_llRuleDescriptions.flatMap(_._2).toSet -- _llRuleDescriptions.map(_._1)).map(Terminal(_))

  def terminals = _terminals

  val terminalsByName = _terminals.map(t => (t.label, t)).toMap

  val _llRules =
    _llRuleDescriptions.zipWithIndex
      .map({ case (desc, id) => LLRule(id, nonTerminalsByName(desc._1), desc._2.map(symbol(_).get)) })

  def llRules = _llRules

  val followMemo = mutable.Map[Symbol, Set[Symbol]]()

  def symbol(label: String) =
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
          case _ => first(rest) ++ Set(ε)
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

  def first(X: Symbol): Set[Symbol] =
    if (terminalsByName.contains(X.label)) {
      Set(X)
    } else {
      llRules.filter(_.from equals X).flatMap({ rule =>
        rule.rhs match {
          case List(ε) => Set(ε) // Case 2
          case _ => first(rule.rhs) // Case 3
        }
      }).toSet
    }

  /**
   *
   * 1. First put ⊥ (the end of input marker) in Follow(S) (S is the start symbol)
   * 2. If there is a production A -> aBb, (where 'a' can be a whole string) then everything in FIRST(b) except for epsilon is placed in FOLLOW(B).
   * 3. If there is a production A -> aB, then everything in FOLLOW(A) is in FOLLOW(B)
   * 4. If there is a production A -> aBb, where FIRST(b) contains epsilon, then everything in FOLLOW(A) is in FOLLOW(B)
   *
   * http://www.scribd.com/doc/7185137/First-and-Follow-Set
   *
   */

  def follow(symbol: NonTerminal): Set[Symbol] = {

    info("computing follow(" + symbol + ")")

    if (followMemo.contains(symbol)) {
      followMemo(symbol)
    } else {
      // TODO allow Terminal(_) in cases below to be a list of Terminals
      val result = (symbol match {
        case Start => Set(⊥)
        case _ => Set()
      }) ++ (llRules.flatMap({
        rule =>
          (rule.rhs match {
            // TODO?: enforce that rest is composed of only terminals (maybe not the case)
            case Terminal(_) :: symbol :: rest => first(rest).filter(x => !(x equals ε))
            case _ => Set()
          }) ++
            (rule.rhs match {
              case Terminal(_) :: symbol :: Nil => follow(rule.from)
              case Terminal(_) :: symbol :: rest if (first(rest).contains(ε)) => follow(rule.from)
              case _ => Set()
            })
      }))
      followMemo.update(symbol, result)
      result
    }
  }

  lazy val _parseTable: Map[(NonTerminal, Symbol), LLRule] = {
    for (nt <- nonTerminals) {
      first(nt)
    }
    llRules.flatMap({ rule =>
      first(rule.rhs).flatMap(a => {
        if (terminalsByName.contains(a.label)) {
          List((rule.from, a) -> rule)
        } else if (a == ε) {
          follow(rule.from).map(t => (rule.from, t) -> rule).toList // TODO including $
        } else {
          Nil
        }
      })
    }).toMap
  }

  def parseTable = _parseTable
  
}
