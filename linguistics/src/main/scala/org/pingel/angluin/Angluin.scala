
package org.pingel.angluin

import scala.collection._

// TODO: rephrase this graph in terms of org.pingel.axle.graph.DirectedGraph

class Acceptor {

  var Q = Set[AcceptorState]()
  var I = Set[AcceptorState]()
  var F = Set[AcceptorState]()
  var outArcs = Map[AcceptorState, Map[Symbol, Set[AcceptorState]]]()
  var inArcs = Map[AcceptorState, Map[Symbol, Set[AcceptorState]]]()

  def addState(p: AcceptorState, isInitial: Boolean, isFinal: Boolean): Unit = {

    Q += p

    if( isInitial )
      I += p

    if( isFinal )
      F += p

    outArcs += p -> Map[Symbol, Set[AcceptorState]]()
    inArcs += p -> Map[Symbol, Set[AcceptorState]]()
  }

  def addTransition(from: AcceptorState, symbol: Symbol, to: AcceptorState): Unit = {

    var symbol2outs = outArcs(symbol)
    var outs: Set[AcceptorState] = null
    if( symbol2outs.contains(symbol) ) {
      outs = symbol2outs(symbol)
    }
    else {
      outs = Set[AcceptorState]()
      symbol2outs += symbol -> outs
    }
    outs += to

    var symbol2ins = inArcs(symbol)
    var ins: Set[AcceptorState] = null
    if( symbol2ins.contains(symbol) ) {
      ins = symbol2ins(symbol)
    }
    else {
      ins = Set[AcceptorState]()
      symbol2ins += symbol -> ins
    }

    ins += from
  }

  def δ(state: AcceptorState, symbol: Symbol): Set[AcceptorState] = outArcs(state)(symbol)

  def δ(state: AcceptorState, exp: Expression): Set[AcceptorState] = {

    var result = Set[AcceptorState]()
    if (exp == null) {
      result += state
    } 
    else {
      var head = exp.getHead()
      var tail = exp.getTail()
      var neighbors = δ(state, head)
      for( neighbor <- neighbors ) {
        result ++= δ(neighbor, tail)
      }
    }
    result
  }
  
  def isForwardDeterministic(): Boolean = {

    if (I.size > 1) {
      return false
    }

    for( state <- Q ) {
      val symbol2outs = outArcs(state)
      val outSymbols = symbol2outs.keys
      for(symbol <- outSymbols ) {
        val outs = symbol2outs(symbol)
        if (outs.size > 1) {
          return false
        }
      }
    }
    true
  }

  def isBackwardDeterministic(): Boolean = {

    if (F.size > 1) {
      return false
    }

    for( state <- Q ) {
      val symbol2ins = inArcs(state)
      val inSymbols = symbol2ins.keys
      for ( symbol <- inSymbols ) {
        val ins = symbol2ins(symbol)
        if (ins.size > 1) {
          return false
        }
      }
    }
    true
  }

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

class AcceptorState { }

class Alphabet { // TODO: redefine as Set[Symbol]

  var symbols = Set[Symbol]()

  def addSymbol(m: Symbol) = {
	  symbols += m
  }

  def iterator() = symbols.iterator
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

class MutableExpression(vs: List[Symbol]) extends Expression {

  var v = new mutable.ListBuffer[Symbol]()

  v ++= vs
  
  def getSymbolIterator() = v.iterator

  def addSymbol(s: Symbol) = { 
    v += s
  }

  def length() = v.size 

  override def getHead() = v(0)

  override def getTail() = new MutableExpression(v.toList.tail)

  def equals(other: Expression): Boolean = {
    // TODO !!!
    false
  }

  override def toString() = "\"" + v.mkString(" ") + "\""

}


class ExpressionComparator extends Comparable[Expression]
{
  def compare(o1: Expression, o2: Expression): Int = (o1.toString()).compareTo(o2.toString())
}

trait Grammar {
  def ℒ(): Language
}

class HardCodedGrammar(ℒ: Language) extends Grammar
{
  // Note: This was orginally a getter called simply ℒ()
  // figure out how to write the extractor (or whatever)
  // to grab this

  def getℒ() = ℒ
}

class HardCodedLearner(T: Text, G: Grammar) extends Learner(T)
{
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    G
  }
}

case class ▦ extends Expression() {
  // should this class throw an exception
  // if addMorpheme is called?
  override def toString() = "▦"
}

class Language {

  var sequences = Set[Expression]() // TOOD: was TreSet with new ExpressionComparator()
  
  def addExpression(s: Expression): Unit = sequences += s
  
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

class Learner(T: Text)
{
  var iterator = T.iterator
  
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    // default implementation never guesses a Grammar
    null
  }
  
  def nextExpression() = iterator.next()
  
  def hasNextExpression() = iterator.hasNext
  
}

class MemorizingLearner(T: Text) extends Learner(T) {

  var runningGuess = new Language()
  
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    s match {
      case ▦() => { }
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

class PartitionBlock { }

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


case class Symbol(s: String, Σ: Alphabet)  {

  Σ.addSymbol(this)
  
  override def toString() = s
  
  def equals(other: Symbol): Boolean = s.equals(other.s)
  
}

case class Text {

  var v = mutable.ListBuffer[Expression]()
  
  def addExpression(s: Expression) = { 
    v += s
  }
  
  def length() = v.size
  
  def isFor(ℒ: Language) = content().equals(ℒ)
  
  def content(): Language = {
    var ℒ = new Language()
    for( s <- v ) {
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
