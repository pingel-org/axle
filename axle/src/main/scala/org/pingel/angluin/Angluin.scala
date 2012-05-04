
package org.pingel.angluin

import scala.collection._

import axle.graph.JungDirectedGraphFactory._
import axle.Enrichments._

case class Acceptor() {

  val g = graph[String, Symbol]()

  type AcceptorState = g.V

  def Q() = g.getVertices

  var I = Set[AcceptorState]()
  var F = Set[AcceptorState]()

  def addState(isInitial: Boolean, isFinal: Boolean): Unit = {

    val p = g += "" // TODO

    if (isInitial)
      I += p

    if (isFinal)
      F += p
  }

  def δ(state: AcceptorState, symbol: Symbol): Set[AcceptorState] =
    g.getEdges.filter(e => e.getSource == state && e.getPayload == symbol).map(_.getDest)

  def δ(state: AcceptorState, exp: Expression): Set[AcceptorState] = {

    var result = Set[AcceptorState]()
    if (exp == null) {
      result += state
    } else {
      var head = exp.getHead()
      var tail = exp.getTail()
      var neighbors = δ(state, head)
      for (neighbor <- neighbors) {
        result ++= δ(neighbor, tail)
      }
    }
    result
  }

  // TODO: not sure if this should count edges or nodes:
  def isForwardDeterministic(): Boolean = (I.size <= 1) && Q.∀(g.getSuccessors(_).size <= 1)

  def isBackwardDeterministic(): Boolean = (F.size <= 1) && Q.∀(g.getPredecessors(_).size <= 1)

  def isZeroReversible(): Boolean = isForwardDeterministic() && isBackwardDeterministic()

  def isIsomorphicTo(other: Acceptor): Boolean = {
    // TODO !!!
    false
  }

  def isSubacceptorOf(other: Acceptor): Boolean = {
    // TODO !!!
    false
  }

  def induce(P: Set[AcceptorState]): Acceptor = {
    // TODO !!!
    null
  }

}

case class CanonicalAcceptorFactory {

  def makeCanonicalAcceptor(ℒ: Language): Acceptor = {
    // TODO !!!
    null
  }

}

trait Expression {

  def getHead(): Symbol

  def getTail(): Expression // List[Symbol]

}

case class MutableExpression(vs: List[Symbol]) extends Expression {

  var v = new mutable.ListBuffer[Symbol]()

  v ++= vs

  def getSymbolIterator() = v.iterator

  def addSymbol(s: Symbol) = v += s

  def length() = v.size

  override def getHead() = v(0)

  override def getTail() = new MutableExpression(v.toList.tail)

  def equals(other: Expression): Boolean = {
    // TODO !!!
    false
  }

  override def toString() = "\"" + v.mkString(" ") + "\""

}

class ExpressionComparator extends Comparable[Expression] {
  def compareTo(other: Expression) = (this.toString()).compareTo(other.toString)
  // def compare(o1: Expression, o2: Expression): Int = (o1.toString()).compareTo(o2.toString())
}

trait Grammar {
  def getℒ(): Language
}

class HardCodedGrammar(ℒ: Language) extends Grammar {
  // Note: This was orginally a getter called simply ℒ()
  // figure out how to write the extractor (or whatever)
  // to grab this

  def getℒ() = ℒ
}

class HardCodedLearner(T: Text, G: Grammar) extends Learner(T) {
  override def processNextExpression(): Grammar = {
    val s = nextExpression()
    G
  }
}

case class ▦() extends Expression() {

  // TOOD: not sure about head and tail here:
  def getHead(): Symbol = null
  def getTail(): Expression = null

  // should this class throw an exception
  // if addMorpheme is called?
  override def toString() = "▦"
}

case class Language(var sequences: List[Expression] = Nil) {

  def addExpression(s: Expression): Unit = {
    sequences = sequences ::: List(s)
  }

  def equals(other: Language): Boolean = sequences.equals(other.sequences)

  def prefixes(): Language = {
    // TODO !!!
    null
  }

  def goodFinals(w: Expression): Language = {
    // TODO !!!
    null
  }

  override def toString() = "{" + sequences.mkString(", ") + "}"

}

class Learner(T: Text) {
  var iterator = T.iterator

  def processNextExpression(): Grammar = {
    val s = nextExpression()
    // default implementation never guesses a Grammar
    null
  }

  def nextExpression() = iterator.next()

  def hasNextExpression() = iterator.hasNext

}

case class MemorizingLearner(T: Text) extends Learner(T) {

  var runningGuess = Language(Nil)

  override def processNextExpression(): Grammar = {
    val s = nextExpression()
    s match {
      case ▦() => {}
      case _ => runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }
}

class Partition {
  def restrictTo(subset: Set[Any]): Partition = {
    // TODO !!!
    return null;
  }
}

class PartitionBlock {}

class PrefixTreeFactory {
  def makePrefixTree(ℒ: Language): Acceptor = {
    // TODO !!!
    null
  }
}

case class Quotient(A: Acceptor, π: Partition) {
  def evaluate(): Acceptor = {
    // TODO !!!
    null
  }
}

case class Symbol(s: String, Σ: mutable.Set[Symbol]) {

  Σ += this

  override def toString() = s

  def equals(other: Symbol): Boolean = s.equals(other.s)

}

case class Text(var v: List[Expression]) {

  def addExpression(s: Expression): Unit = v = v ::: List(s)

  def length() = v.size

  def isFor(ℒ: Language) = content().equals(ℒ)

  def content(): Language = {
    var ℒ = new Language()
    for (s <- v) {
      s match {
        case ▦() => {}
        case _ => ℒ.addExpression(s)
      }
    }
    ℒ
  }

  def iterator = v.iterator

  override def toString() = "<" + v.mkString(", ") + ">"

}
