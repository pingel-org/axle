
package axle.lx

import axle.Show
import axle.jung.JungDirectedGraph
import axle.graph.Vertex
import axle.string
import spire.algebra.Eq
import spire.algebra.Order
import spire.implicits.StringOrder
import spire.implicits.eqOps

object Angluin {

  case class Symbol(s: String)

  object Symbol {

    implicit val symbolEq = new Eq[Symbol] {
      def eqv(x: Symbol, y: Symbol): Boolean = x equals y
    }

    implicit def showSymbol: Show[Symbol] = new Show[Symbol] {
      def text(s: Symbol): String = s.s
    }

    implicit val orderSymbol: Order[Symbol] = new Order[Symbol] {
      def compare(x: Symbol, y: Symbol): Int = string(x).compareTo(string(y))
    }

  }

  class AngluinAcceptor(vps: Seq[String], I: Set[String], F: Set[String]) {

    val graph = JungDirectedGraph[String, Symbol](vps, vs => Nil)

    def Q: Set[Vertex[String]] = graph.vertices

    //    def addState(isInitial: Boolean, isFinal: Boolean): Acceptor = {
    //      val (newG, v) = g + "" // TODO
    //      val newI = isInitial ? (I + v.payload) | I
    //      val newF = isFinal ? (F + v.payload) | F
    //      Acceptor(newG, newI, newF)
    //    }

    def δSymbol(state: Vertex[String], symbol: Symbol): Set[Vertex[String]] =
      graph.allEdges.collect({ case e if graph.source(e) === state && e.payload === symbol => graph.dest(e) })

    def δ(state: Vertex[String], exp: List[Symbol]): Set[String] = exp match {
      case head :: tail => δSymbol(state, head).map(δ(_, tail)).reduce(_ ++ _)
      case Nil          => Set(state.payload)
    }

    // TODO: not sure if this should count edges or nodes:
    //    def isForwardDeterministic(): Boolean = (I.size <= 1) && Q.∀(successors(_).size <= 1)
    //    def isBackwardDeterministic(): Boolean = (F.size <= 1) && Q.∀(predecessors(_).size <= 1)
    //    def isZeroReversible(): Boolean = isForwardDeterministic() && isBackwardDeterministic()

    def isIsomorphicTo(other: AngluinAcceptor): Boolean = ???

    def isSubacceptorOf(other: AngluinAcceptor): Boolean = ???

    def induce(P: Set[Vertex[String]]): AngluinAcceptor = ???

  }

  // type Expression = List[Symbol]

  val ♯ = List[Symbol]()

  // val g = graph[String, Symbol]()

  case class CanonicalAcceptorFactory() {

    def makeCanonicalAcceptor(ℒ: Language): AngluinAcceptor = ???

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
      def text(l: Language): String = "{" + l.sequences.mkString(", ") + "}"
    }
  }

  trait Learner[S] {

    def initialState: S

    def processExpression(state: S, expression: Iterable[Symbol]): (S, Option[Grammar])

    val noGuess = None.asInstanceOf[Option[Grammar]]

    def guesses(T: Text): Iterator[Grammar] =
      T.expressions.iterator
        .scanLeft((initialState, noGuess))((sg, e) => processExpression(sg._1, e))
        .flatMap(_._2)
  }

  /**
   * The SilentLearner never makes a guess
   */

  case class SilentLearner(T: Text) extends Learner[Nothing] {

    def initialState() = null.asInstanceOf[Nothing]

    def processExpression(state: Nothing, expression: Iterable[Symbol]) = (initialState, None)
  }

  /**
   * The HardCodedLearner always guesses the same thing
   */

  case class HardCodedLearner(G: Grammar) extends Learner[Nothing] {

    def initialState: Nothing = null.asInstanceOf[Nothing]

    def processExpression(state: Nothing, expression: Iterable[Symbol]): (Nothing, Option[Grammar]) =
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

  class Partition {
    def restrictTo(subset: Set[Any]): Partition = ???
  }

  class PartitionBlock {}

  class PrefixTreeFactory {
    def makePrefixTree(ℒ: Language): AngluinAcceptor = ???
  }

  case class Quotient(A: AngluinAcceptor, π: Partition) {
    def evaluate: AngluinAcceptor = ???
  }

  // implicit def enAlphabet(symbols: Set[Symbol]): Alphabet = Alphabet(symbols)

  case class Alphabet(symbols: Set[Symbol])

  // implicit def enText(expressions: Iterable[Iterable[Symbol]]): Text = Text(expressions)

  case class Text(expressions: Iterable[Iterable[Symbol]]) {

    // def addExpression(s: List[Symbol]): Unit = expressions = expressions ::: List(s)

    def length: Int = expressions.size

    def isFor(ℒ: Language) = content === ℒ

    def content: Language = new Language(expressions.filter(_ != ♯))
  }

  object Text {
    implicit def showText: Show[Text] = new Show[Text] {
      def text(t: Text): String = "<" + t.expressions.mkString(", ") + ">"
    }
  }

}
