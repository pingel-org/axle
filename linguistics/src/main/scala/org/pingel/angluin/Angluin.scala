
package org.pingel.angluin

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

  def δ(state: AcceptorState, symbol: Symbol) = outArcs(state)(symbol)

  def δ(state: AcceptorState, exp: Expression) = {

    var result = Set[AcceptorState]()
    if (exp == null) {
      result += state
    } 
    else {
      var head = exp.getHead()
      var tail = exp.getTail()
      var neighbors = δ(state, head)
      for( neighbor <- neighbors ) {
        result.addAll( δ(neighbor, tail) )
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

class CanonicalAcceptorFactory {

  def makeCanonicalAcceptor(ℒ: Language): Acceptor = {
    // TODO !!!
    null
  }

}

class Expression(v: List[Symbol]) {

  def getSymbolIterator() = v.iterator

  def addSymbol(s: Symbol) = { 
    v = v ::: List(s)
  }

  def length() = v.size 

  def getHead() = v(0)

  def getTail() = new Expression(v.tail)

  def equals(other: Expression): Boolean = {
    // TODO !!!
    false
  }

  override def toString() = "\"" + v.mkString(" ") + "\""

}


class ExpressionComparator extends Comparator[Expression]
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

object ▦ extends Expression {
  // should this class throw an exception
  // if addMorpheme is called?
  override def toString() = "▦"
}

class Language {

  var sequences = TreeSet[Expression](new ExpressionComparator())
  
  def addExpression(s: Expression): Unit = sequences.add(s)
  
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
      case _: ▦ =>
      case _ => runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }
}

class Partition {
  def restrictTo(subset: Set[TODO]): Partition = {
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

class Quotient(A: Acceptor, π: Partition) {
  def evaluate(): Acceptor = {
    // TODO !!!
    null
  }
}


class Symbol(s: String, Σ: Alphabet)  {

  Σ.addSymbol(this)
  
  override def toString() = s
  
  def equals(other: Symbol): Boolean = s.equals(other.s)
  
}

class Text {

  var v = List[Expression]()
  
  def addExpression(s: Expression) = { 
    v += s
  }
  
  def length() = v.size
  
  def isFor(ℒ: Language) = content().equals(ℒ)
  
  def content(): Language = {
    var ℒ = new Language()
    for( s <- v ) {
      s match {
        case _: ▦ =>
        case _ => ℒ.addExpression(s)
      }
    }
    ℒ
  }

  def iterator = v.iterator
  
  override def toString() = "<" + v.mkString(", ") + ">"
  
}

object test {
  
  def main(args: List[String]): Unit = {

    var Σ = new Alphabet()
    
    val mHi = new Symbol("hi", Σ)
    val mIm = new Symbol("I'm", Σ)
    val mYour = new Symbol("your", Σ)
    val mMother = new Symbol("Mother", Σ)
    val mShut = new Symbol("shut", Σ)
    val mUp = new Symbol("up", Σ)
    
    val s1 = new Expression( List(mHi, mIm, mYour, mMother) )
    val s2 = new Expression( List(mShut, mUp) )
    val ℒ = new Language( List(s1, s2) )
    
    val T = new Text( List(s1, ▦, ▦, s2, ▦, s2, s2) )
    
    println("Text T = " + T )
    println("Language ℒ = " + ℒ )
    println()
    
    if( T.isFor(ℒ) ) {
      println("T is for ℒ")
    }
    else {
      println("T is not for ℒ")
    }
    println()
    
    var ɸ = new MemorizingLearner(T)
    
    var guess: Grammar = null
    
    while( ɸ.hasNextExpression() ) {
      guess = ɸ.processNextExpression()
      if( guess != null ) {
        val guessedLanguage = guess.ℒ()
        println("ɸ.processNextExpression().ℒ = " + guessedLanguage )
        if( guessedLanguage.equals(ℒ) ) {
          println("ɸ identified the language using the text")
          exit(0)
        }
        else {
          println("ɸ's guess was not correct\n")
        }
      }
    }
    
    if ( guess == null ) {
      println("ɸ never made a guess");
    }
    
  }
  
}
