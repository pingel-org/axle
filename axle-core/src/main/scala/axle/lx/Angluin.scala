
package axle.lx

import cats.Show
import axle.algebra.DirectedGraph
import axle.string
import axle.syntax.directedgraph._
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

object Angluin {

  case class Symbol(s: String)

  object Symbol {

    implicit val symbolEq = new Eq[Symbol] {
      def eqv(x: Symbol, y: Symbol): Boolean = x equals y
    }

    implicit def showSymbol: Show[Symbol] = new Show[Symbol] {
      def show(s: Symbol): String = s.s
    }

    implicit val orderSymbol: Order[Symbol] = new Order[Symbol] {
      def compare(x: Symbol, y: Symbol): Int = string(x).compareTo(string(y))
    }

  }

  case class AngluinAcceptor[DG](vps: Seq[String], I: Set[String], F: Set[String])(
      implicit evDG: DirectedGraph[DG, String, Symbol]) {

    val graph = evDG.make(vps, Nil)

    def Q: Set[String] = graph.vertices.toSet

    //    def addState(isInitial: Boolean, isFinal: Boolean): Acceptor = {
    //      val (newG, v) = g + "" // TODO
    //      val newI = isInitial ? (I + v.payload) | I
    //      val newF = isFinal ? (F + v.payload) | F
    //      Acceptor(newG, newI, newF)
    //    }

    def δSymbol(state: String, symbol: Symbol): Set[String] =
      graph.edges.collect({ case e if (graph.source(e) === state && Symbol.symbolEq.eqv(e, symbol)) => graph.destination(e) }).toSet

    def δ(state: String, exp: List[Symbol]): Set[String] = exp match {
      case head :: tail => δSymbol(state, head).map(δ(_, tail)).reduce(_ ++ _)
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

  // type Expression = List[Symbol]

  val ♯ = List.empty[Symbol]

  // val g = graph[String, Symbol]()

  case class CanonicalAcceptorFactory() {

    def makeCanonicalAcceptor[DG](ℒ: Language): AngluinAcceptor[DG] = ???

  }

  trait Grammar {
    def ℒ: Language
  }

  case class HardCodedGrammar(_ℒ: Language) extends Grammar {
    // Note: This was orginally a getter called simply ℒ()
    // figure out how to write the extractor (or whatever)
    // to grab this

    def ℒ = _ℒ
  }

  case class Language(sequences: Iterable[Iterable[Symbol]] = Nil) {

    def prefixes: Language = ???

    def goodFinals(w: List[Symbol]): Language = ???
  }

  object Language {
    implicit val languageEq = new Eq[Language] {
      def eqv(x: Language, y: Language): Boolean = x.sequences.equals(y.sequences)
    }
    implicit def showLanguage: Show[Language] = new Show[Language] {
      def show(l: Language): String = "{" + l.sequences.mkString(", ") + "}"
    }
  }

  trait Learner[S] {

    def initialState: S

    def processExpression(state: S, expression: Iterable[Symbol]): (S, Option[Grammar])

    val noGuess = Option.empty[Grammar]

    def guesses(T: Text): Iterator[Grammar] =
      T.expressions.iterator
        .scanLeft((initialState, noGuess))((sg, e) => processExpression(sg._1, e))
        .flatMap(_._2)
  }

  /**
   * The SilentLearner never makes a guess
   */

  case class SilentLearner(T: Text) extends Learner[Unit] {

    def initialState: Unit = {}

    def processExpression(state: Unit, expression: Iterable[Symbol]) =
      (initialState, None)
  }

  /**
   * The HardCodedLearner always guesses the same thing
   */

  case class HardCodedLearner(G: Grammar) extends Learner[Unit] {

    def initialState: Unit = {}

    def processExpression(state: Unit, expression: Iterable[Symbol]): (Unit, Option[Grammar]) =
      (initialState, Some(G))
  }

  /**
   * The MemorizingLearner accrues expressions
   */

  case class MemorizingLearner() extends Learner[Language] {

    def initialState: Language = Language(Nil)

    def processExpression(state: Language, expression: Iterable[Symbol]): (Language, Option[Grammar]) =
      expression match {
        case ♯ => (state, Some(HardCodedGrammar(state)))
        case _ => {
          val newState = Language(state.sequences ++ List(expression))
          (newState, Some(HardCodedGrammar(newState)))
        }
      }

  }

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

  // implicit def enAlphabet(symbols: Set[Symbol]): Alphabet = Alphabet(symbols)

  case class Alphabet(symbols: Set[Symbol])

  // implicit def enText(expressions: Iterable[Iterable[Symbol]]): Text = Text(expressions)

  case class Text(expressions: Iterable[Iterable[Symbol]]) {

    // def addExpression(s: List[Symbol]): Unit = expressions = expressions ::: List(s)

    def length: Int = expressions.size

    def isFor(ℒ: Language) = content === ℒ

    def content: Language = Language(expressions.filter(_ != ♯))
  }

  object Text {
    implicit def showText: Show[Text] = new Show[Text] {
      def show(t: Text): String = "<" + t.expressions.mkString(", ") + ">"
    }
  }

}
