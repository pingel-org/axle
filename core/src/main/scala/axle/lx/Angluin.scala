
package axle.lx

import collection._
import scalaz._
import Scalaz._
import axle.graph.JungDirectedGraph._

class AngluinAcceptor(vps: Seq[String], I: Set[String], F: Set[String])
  extends JungDirectedGraph[String, Symbol](vps, vs => Nil) {

  def Q(): Set[JungDirectedGraphVertex[String]] = vertices()

  //    def addState(isInitial: Boolean, isFinal: Boolean): Acceptor = {
  //      val (newG, v) = g + "" // TODO
  //      val newI = isInitial ? (I + v.payload) | I
  //      val newF = isFinal ? (F + v.payload) | F
  //      Acceptor(newG, newI, newF)
  //    }

  def δSymbol(state: JungDirectedGraphVertex[String], symbol: Symbol): Set[JungDirectedGraphVertex[String]] =
    edges().filter(e => e.source == state && e.payload == symbol).map(_.dest)

  def δ(state: JungDirectedGraphVertex[String], exp: List[Symbol]): Set[String] = exp match {
    case head :: tail => δSymbol(state, head).map(δ(_, tail)).reduce(_ ++ _)
    case Nil => Set(state.payload)
  }

  // TODO: not sure if this should count edges or nodes:
  //    def isForwardDeterministic(): Boolean = (I.size <= 1) && Q.∀(successors(_).size <= 1)
  //    def isBackwardDeterministic(): Boolean = (F.size <= 1) && Q.∀(predecessors(_).size <= 1)
  //    def isZeroReversible(): Boolean = isForwardDeterministic() && isBackwardDeterministic()

  def isIsomorphicTo(other: AngluinAcceptor): Boolean = {
    // TODO !!!
    false
  }

  def isSubacceptorOf(other: AngluinAcceptor): Boolean = {
    // TODO !!!
    false
  }

  def induce(P: Set[JungDirectedGraphVertex[String]]): AngluinAcceptor = {
    // TODO !!!
    null
  }

}

object Angluin {

  // type Expression = List[Symbol]

  val ▦ = List[Symbol]()

  // val g = graph[String, Symbol]()

  case class CanonicalAcceptorFactory() {

    def makeCanonicalAcceptor(ℒ: Language): AngluinAcceptor = {
      // TODO !!!
      null
    }

  }

  class ExpressionComparator extends Comparable[List[Symbol]] {
    def compareTo(other: List[Symbol]) = (this.toString()).compareTo(other.toString)
    // def compare(o1: Expression, o2: Expression): Int = (o1.toString()).compareTo(o2.toString())
  }

  trait Grammar {
    def ℒ(): Language
  }

  class HardCodedGrammar(_ℒ: Language) extends Grammar {
    // Note: This was orginally a getter called simply ℒ()
    // figure out how to write the extractor (or whatever)
    // to grab this

    def ℒ() = _ℒ
  }

  class HardCodedLearner(T: Text, G: Grammar) extends Learner(T) {
    override def processExpression(e: List[Symbol]): Option[Grammar] = {
      val s = e
      Some(G)
    }
  }

  case class Language(sequences: List[List[Symbol]] = Nil) {

    def equals(other: Language): Boolean = sequences.equals(other.sequences)

    def prefixes(): Language = {
      // TODO !!!
      null
    }

    def goodFinals(w: List[Symbol]): Language = {
      // TODO !!!
      null
    }

    override def toString() = "{" + sequences.mkString(", ") + "}"

  }

  class Learner(T: Text) {

    def processExpression(e: List[Symbol]): Option[Grammar] = {
      val s = e
      // default implementation never guesses a Grammar
      None
    }

    def guesses(): Iterator[Grammar] = T.expressions.iterator.flatMap(processExpression(_))

  }

  case class MemorizingLearner(T: Text) extends Learner(T) {

    var _runningGuess = Language(Nil)

    override def processExpression(e: List[Symbol]): Option[Grammar] = {
      if (e != ▦) {
        _runningGuess = new Language(_runningGuess.sequences ++ List(e))
      }
      Some(new HardCodedGrammar(_runningGuess))
    }

  }

  class Partition {
    def restrictTo(subset: Set[Any]): Partition = {
      // TODO !!!
      return null
    }
  }

  class PartitionBlock {}

  class PrefixTreeFactory {
    def makePrefixTree(ℒ: Language): AngluinAcceptor = {
      // TODO !!!
      null
    }
  }

  case class Quotient(A: AngluinAcceptor, π: Partition) {
    def evaluate(): AngluinAcceptor = {
      // TODO !!!
      null
    }
  }

  case class Alphabet() {

    val symbols = mutable.Set[Symbol]()

    def symbol(s: String): Symbol = {
      val symbol = new Symbol(s)
      symbols += symbol
      symbol
    }

  }

  case class Symbol(s: String) {

    override def toString() = s

    def equals(other: Symbol): Boolean = s.equals(other.s)

  }

  case class Text(var expressions: List[List[Symbol]]) {

    def addExpression(s: List[Symbol]): Unit = expressions = expressions ::: List(s)

    def length() = expressions.size

    def isFor(ℒ: Language) = content().equals(ℒ)

    def content(): Language = new Language(expressions.filter(_ != ▦))

    override def toString() = "<" + expressions.mkString(", ") + ">"

  }

}
