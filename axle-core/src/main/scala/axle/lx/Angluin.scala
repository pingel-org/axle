
package axle.lx

import axle._
import axle.graph._
import spire.math._
import spire.algebra._
import spire.implicits._

class AngluinAcceptor(vps: Seq[String], I: Set[String], F: Set[String]) {

  import Angluin._
  implicit val se = Symbol.symbolEq // TODO get rid of this
  
  val graph = JungDirectedGraph[String, Symbol](vps, vs => Nil)

  import graph._

  def Q() = vertices()

  //    def addState(isInitial: Boolean, isFinal: Boolean): Acceptor = {
  //      val (newG, v) = g + "" // TODO
  //      val newI = isInitial ? (I + v.payload) | I
  //      val newF = isFinal ? (F + v.payload) | F
  //      Acceptor(newG, newI, newF)
  //    }

  def δSymbol(state: Vertex[String], symbol: Symbol): Set[Vertex[String]] =
    allEdges().filter(e => source(e) == state && e.payload == symbol).map(dest(_))

  def δ(state: Vertex[String], exp: List[Symbol]): Set[String] = exp match {
    case head :: tail => δSymbol(state, head).map(δ(_, tail)).reduce(_ ++ _)
    case Nil => Set(state.payload)
  }

  // TODO: not sure if this should count edges or nodes:
  //    def isForwardDeterministic(): Boolean = (I.size <= 1) && Q.∀(successors(_).size <= 1)
  //    def isBackwardDeterministic(): Boolean = (F.size <= 1) && Q.∀(predecessors(_).size <= 1)
  //    def isZeroReversible(): Boolean = isForwardDeterministic() && isBackwardDeterministic()

  def isIsomorphicTo(other: AngluinAcceptor): Boolean = ???

  def isSubacceptorOf(other: AngluinAcceptor): Boolean = ???

  def induce(P: Set[Vertex[String]]): AngluinAcceptor = ???

}

object Angluin {

  // type Expression = List[Symbol]

  val ♯ = List[Symbol]()

  // val g = graph[String, Symbol]()

  case class CanonicalAcceptorFactory() {

    def makeCanonicalAcceptor(ℒ: Language): AngluinAcceptor = ???

  }

  class ExpressionComparator extends Comparable[List[Symbol]] {
    def compareTo(other: List[Symbol]) = (this.toString()).compareTo(other.toString)
    // def compare(o1: Expression, o2: Expression): Int = (o1.toString()).compareTo(o2.toString())
  }

  trait Grammar {
    def ℒ(): Language
  }

  case class HardCodedGrammar(_ℒ: Language) extends Grammar {
    // Note: This was orginally a getter called simply ℒ()
    // figure out how to write the extractor (or whatever)
    // to grab this

    def ℒ() = _ℒ
  }

  case class Language(sequences: Iterable[Iterable[Symbol]] = Nil) {

    def prefixes(): Language = ???

    def goodFinals(w: List[Symbol]): Language = ???

    override def toString() = "{" + sequences.mkString(", ") + "}"

  }

  object Language {
    implicit val languageEq = new Eq[Language] {
      def eqv(x: Language, y: Language): Boolean = x.sequences.equals(y.sequences)
    }
  }
  
  trait Learner[S] {

    def initialState(): S

    def processExpression(state: S, expression: Iterable[Symbol]): (S, Option[Grammar])

    val noGuess = None.asInstanceOf[Option[Grammar]]

    def guesses(T: Text): Iterator[Grammar] =
      T.expressions.iterator
        .scanLeft((initialState(), noGuess))((sg, e) => processExpression(sg._1, e))
        .flatMap(_._2)
  }

  /**
   * The SilentLearner never makes a guess
   */

  case class SilentLearner(T: Text) extends Learner[Nothing] {

    def initialState() = null.asInstanceOf[Nothing]

    def processExpression(state: Nothing, expression: Iterable[Symbol]) = (initialState(), None)
  }

  /**
   * The HardCodedLearner always guesses the same thing
   */

  case class HardCodedLearner(G: Grammar) extends Learner[Nothing] {

    def initialState() = null.asInstanceOf[Nothing]

    def processExpression(state: Nothing, expression: Iterable[Symbol]) = (initialState(), Some(G))
  }

  /**
   * The MemorizingLearner accrues expressions
   */

  case class MemorizingLearner() extends Learner[Language] {

    def initialState() = Language(Nil)

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
    def evaluate(): AngluinAcceptor = ???
  }

  // implicit def enAlphabet(symbols: Set[Symbol]): Alphabet = Alphabet(symbols)

  case class Alphabet(symbols: Set[Symbol])

  case class Symbol(s: String) {
    override def toString() = s
  }

  object Symbol {
    implicit val symbolEq = new Eq[Symbol] {
      def eqv(x: Symbol, y: Symbol): Boolean = x equals y
    }
    
  }
  
  implicit val symbolEq = new Eq[Symbol] {
    def eqv(x: Symbol, y: Symbol): Boolean = x equals y
  }

  // implicit def enText(expressions: Iterable[Iterable[Symbol]]): Text = Text(expressions)

  case class Text(expressions: Iterable[Iterable[Symbol]]) {

    // def addExpression(s: List[Symbol]): Unit = expressions = expressions ::: List(s)

    def length() = expressions.size

    def isFor(ℒ: Language) = content === ℒ

    def content: Language = new Language(expressions.filter(_ != ♯))

    override def toString() = "<" + expressions.mkString(", ") + ">"

  }

}
