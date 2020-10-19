
package axle.lx

import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

import axle.algebra.DirectedGraph
import axle.syntax.directedgraph._

/**
 * 
 * TODO
 * 
 * Still needs something that evaluates an Acceptor,
 * and returns an enumeration the defines the language.
 *
 * Language
 * 
 * EnumerableLanguage
 *
 * FiniteLanguage
 *
 * perhaps I should change Language to ExpressionSet, and redefine
 * a Language to be either finite, in which case it is defined by an ExpressionSet
 * or infinite, in which case it is defined by an AcceptorEnumeration
 * 
 *
 * Be sure that symbols 'a' and 'a' are counted as the same, even if different
 * objects. (define SymbolComparator?)
 *
 * Also need ExpressionComparator?
 * 
 * Transitions should be defined on expressions, not symbols, in order to
 * be maximally general
 * 
 */

object Angluin {

  implicit val orderSymbol: Order[Symbol] =
    (x, y) => Show[Symbol].show(x).compareTo(Show[Symbol].show(y))

  case class AngluinAcceptor[DG](vps: Seq[String], I: Set[String], F: Set[String])(
    implicit
    evDG: DirectedGraph[DG, String, Symbol]) {

    val graph = evDG.make(vps, Nil)

    def Q: Set[String] = graph.vertices.toSet

    //    def addState(isInitial: Boolean, isFinal: Boolean): Acceptor = {
    //      val (newG, v) = g + "" // TODO
    //      val newI = isInitial ? (I + v.payload) | I
    //      val newF = isFinal ? (F + v.payload) | F
    //      Acceptor(newG, newI, newF)
    //    }

    def δSymbol(state: String, symbol: Symbol): Set[String] =
      graph.edges.collect({ case e if ( (graph.source(e) === state) && (e === symbol) ) => graph.destination(e) }).toSet

    def δ(state: String, exp: Expression): Set[String] = exp.symbols match {
      case head :: tail => δSymbol(state, head).map(δ(_, Expression(tail))).reduce(_ ++ _)
      case Nil          => Set(state)
    }

    // TODO: not sure if this should count edges or nodes:
    //    def isForwardDeterministic(): Boolean = (I.size <= 1) && Q.∀(successors(_).size <= 1)
    //    def isBackwardDeterministic(): Boolean = (F.size <= 1) && Q.∀(predecessors(_).size <= 1)
    //    def isZeroReversible(): Boolean = isForwardDeterministic() && isBackwardDeterministic()

    def isIsomorphicTo(other: AngluinAcceptor[DG]): Boolean = ???

    def isSubacceptorOf(other: AngluinAcceptor[DG]): Boolean = ???

    def induce(P: Set[String]): AngluinAcceptor[DG] = ???

  }

  case class Expression(symbols: List[Symbol])

  object Expression {

    implicit val showExpression: Show[Expression] = 
      _.symbols.map(_.show).mkString(", ")
  }


  val ♯ = Expression(List.empty)

  // val g = graph[String, Symbol]()

  case class CanonicalAcceptorFactory() {

    def makeCanonicalAcceptor[DG](ℒ: Language): AngluinAcceptor[DG] = ???

  }

  trait Grammar {
    def ℒ: Language
  }

  case class HardCodedGrammar(ℒ: Language) extends Grammar

  case class Language(sequences: Set[Expression] = Set.empty) {

    def prefixes: Language = ???

    def goodFinals(w: List[Symbol]): Language = ???
  }

  object Language {

    implicit val languageEq: Eq[Language] = (x, y) => x.sequences.equals(y.sequences)

    implicit def showLanguage: Show[Language] = l => "{" + l.sequences.mkString(", ") + "}"
  }

  class Learner[S](
    initialState: S,
    learnFrom: (S, Expression) => (S, Option[Grammar])) {

    def guesses(T: Text): Iterator[Grammar] =
      T.expressions.iterator
        .scanLeft((initialState, Option.empty[Grammar]))((sg, e) => learnFrom(sg._1, e))
        .flatMap(_._2)
  }

  /**
   * The SilentLearner never makes a guess
   */

  val silentLearner: Learner[Unit] =
    new Learner[Unit](
      (),
      (state, expression) => ((), None)
    )

  /**
   * The HardCodedLearner always guesses the same thing
   */

  def hardCodedLearner(G: Grammar) = new Learner[Unit](
    (),
    (state, expression) => ((), Some(G))
  )

  /**
   * The MemorizingLearner accrues expressions
   */

  val memorizingLearner = new Learner[Language] (
    Language(Set.empty),
    (state, expression) =>
      expression match {
        case ♯ => (state, Some(HardCodedGrammar(state)))
        case _ => {
          val newState = Language(state.sequences ++ List(expression))
          (newState, Some(HardCodedGrammar(newState)))
        }
      }
  )

  case class Partition() {
    def restrictTo(subset: Set[Any]): Partition = ???
  }

  case class PartitionBlock() {}

  case class PrefixTreeFactory() {
    def makePrefixTree[DG](ℒ: Language): AngluinAcceptor[DG] = ???
  }

  case class Quotient[DG](A: AngluinAcceptor[DG], π: Partition) {
    def evaluate: AngluinAcceptor[DG] = ???
  }

  case class Alphabet(symbols: Set[Symbol])

  case class Text(expressions: Iterable[Expression]) extends Iterable[Expression] {

    def iterator: Iterator[Expression] = expressions.iterator

    def isFor(ℒ: Language) = content === ℒ

    val content: Language = Language(expressions.filter(_ != ♯).toSet)
  }

  object Text {
    implicit def showText: Show[Text] = t => "<" + t.expressions.map(_.show).mkString(", ") + ">"
  }

}
