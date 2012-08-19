
package axle.ast

import util.matching.Regex
import collection._

class LLLanguage(override val name: String) extends Language(
  name,
  new Rule("EmptyNode", Nop()) :: Nil,
  Nil,
  (text: String) => None,
  ast => ast
) {
  val nonTerminals = mutable.Map[String, NonTerminal]()
  nonTerminals += Start.label -> Start

  val terminals = mutable.Map[String, Terminal]()
  terminals += BottomOfStack.label -> BottomOfStack

  val llRules = mutable.Map[String, LLRule]()

  val followMemo = mutable.Map[Symbol, Set[Symbol]]()

  val parseTable = mutable.Map[Tuple2[NonTerminal, Symbol], LLRule]()

  def getSymbol(label: String) = terminals.contains(label) match {
    case true => terminals.get(label)
    case false => nonTerminals.get(label)
  }

  def first(XS: List[Symbol]): mutable.Set[Symbol] = XS match {

    // 4. First(Y1Y2..Yk) is either
    //    1. First(Y1) (if First(Y1) doesn't contain epsilon)
    //    2. OR (if First(Y1) does contain epsilon) then First (Y1Y2..Yk) is everything in First(Y1) <except for epsilon > as well as everything in First(Y2..Yk)
    //    3. If First(Y1) First(Y2)..First(Yk) all contain epsilon then add epsilon to First(Y1Y2..Yk) as well.

    case Nil => mutable.Set[Symbol]()

    case head :: rest => {
      val result = first(head)
      if (!result.contains(Epsilon)) {
        result
      } else {
        rest match {
          case Nil => result
          case _ => {
            val rh_result = first(rest)
            if (!rh_result.contains(Epsilon)) {
              result -= Epsilon
            }
            result ++= rh_result
            result
          }
        }
      }
    }
  }

  def first(X: Symbol): mutable.Set[Symbol] = {

    // 1. If X is a terminal then First(X) is just X
    terminals.contains(X.getLabel) match {
      case true => mutable.Set(X)
      case false => {
        val result = mutable.Set[Symbol]()
        for ((_, rule) <- llRules) {
          if (rule.from == X) { // TODO style: use pattern matching
            rule.rhs match {
              case List(Epsilon) => {
                // 2. If there is a Production X -> epsilon then add epsilon to first(X)
                result += Epsilon
              }
              case _ => {
                // 3. If there is a Production X -> Y1Y2..Yk then add first(Y1Y2..Yk) to first(X)
                result ++= first(rule.rhs)
              }
            }
          }
        }
        result
      }
    }
  }

  def follow(symbol: NonTerminal): Set[Symbol] = {

    info("computing follow(" + symbol + ")")

    followMemo.contains(symbol) match {
      case true => followMemo(symbol)
      case false => {

        val result = mutable.Set[Symbol]()

        symbol match {
          case Start => {
            // 1. First put $ (the end of input marker) in Follow(S) (S is the start symbol)
            result += BottomOfStack
          }
          case _ =>
        }

        // 2. If there is a production A -> aBb, (where 'a' can be a whole string) then everything in FIRST(b) except for epsilon is placed in FOLLOW(B).
        // 3. If there is a production A -> aB, then everything in FOLLOW(A) is in FOLLOW(B)
        // 4. If there is a production A -> aBb, where FIRST(b) contains epsilon, then everything in FOLLOW(A) is in FOLLOW(B)	  
        // http://www.scribd.com/doc/7185137/First-and-Follow-Set

        // TODO allow Terminal(_) in cases below to be a list of Terminals

        for ((_, rule) <- llRules) {
          rule.rhs match {
            case Terminal(_) :: symbol :: rest => {
              // to do?: enforce that rest is composed of only terminals (maybe not the case)
              val foo = first(rest)
              if (foo.contains(Epsilon)) {
                foo -= Epsilon
              }
              result ++= foo
            }
            case _ => {}
          }
          rule.rhs match {
            case Terminal(_) :: symbol :: Nil => {
              result ++= follow(rule.from)
            }
            case Terminal(_) :: symbol :: rest if (first(rest).contains(Epsilon)) => {
              result ++= follow(rule.from)
            }
            case _ => {}
          }
        }

        followMemo.update(symbol, result)

        result
      }
    }
  }

  def buildParseTable(): Unit = {

    for ((_, nt) <- nonTerminals) {
      info("computing first(" + nt + ")")
      first(nt)
    }

    for ((id, rule) <- llRules) {
      for (a <- first(rule.rhs)) {
        if (terminals.contains(a.getLabel)) {
          parseTable += (rule.from, a) -> rule // TODO warn on overwrite
        } else if (a == Epsilon) {
          for (t <- follow(rule.from)) {
            // TODO including $
            parseTable += (rule.from, t) -> rule // TODO warn on overwrite
          }
        }
      }
    }

  }

}
