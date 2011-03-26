
package org.pingel.angluin

class Acceptor {

  var Q = Set[AcceptorState]()
  var I = Set[AcceptorState]()
  var F = Set[AcceptorState]()
  var outArcs = Map[AcceptorState, Map[Symbol, Set[AcceptorState]]]()
  var inArcs = Map[AcceptorState, Map[Symbol, Set[AcceptorState]]]()

  def addState(p: AcceptorState, isInitial: Boolean, isFinal: Boolean): Unit = {

    Q.add(p)

    if( isInitial )
      I.add(p)

    if( isFinal )
      F.add(p)
    
    outArcs += p -> Map[Symbol, Set[AcceptorState]]()
    inArcs += p -> Map[Symbol, Set[AcceptorState]]()
  }

  def addTransition(from: AcceptorState, symbol: Symbol, to: AcceptorState): Unit = {

    var symbol2outs = outArcs(symbol)
    var outs = null
    if( symbol2outs.contains(symbol) ) {
      symbol2outs(symbol)
    }
    else {
      outs = Set[AcceptorState]()
      symbol2outs += symbol -> outs
    }
    outs.add(to)

    var symbol2ins = inArcs(symbol)
    var ins = null
    if( symbol2ins.contains(symbol) ) {
      symbol2ins(symbol)
    }
    else {
      ins = Set[AcceptorState]()
      symbol2ins += symbol -> ins
    }

    ins.add(from)
  }

  def δ(state: AcceptorState, symbol: Symbol): Set[AcceptorState] = outArcs(state)(symbol)

  def δ(state: AcceptorState, exp: Expression): Set[AcceptorState] = {

    var result = Set[AcceptorState]()
    if (exp == null) {
      result.add(state)
    } 
    else {
      var head = exp.getHead()
      var tail = exp.getTail()
      var neighbors = δ(state, head)
      val neighbor_it = neighbors.iterator()
      while( neighbor_it.hasNext() ) {
        val neighbor = neighbor_it.next()
        result.addAll( δ(neighbor, tail) )
      }
    }
    result
  }
  
  def isForwardDeterministic(): Boolean = {

    if (I.size() > 1) {
      return false
    }

    val state_it = Q.iterator()
    while( state_it.hasNext() ) {
      val state = state_it.next()
      val symbol2outs = outArcs(state)
      val outSymbols = symbol2outs.keySet()
      val outSymbolIt = outSymbols.iterator()
      while (outSymbolIt.hasNext()) {
        val symbol = outSymbolIt.next()
        val outs = symbol2outs(symbol)
        if (outs.size() > 1) {
          return false
        }
      }
    }
    true
  }

  def isBackwardDeterministic(): Boolean = {

    if (F.size() > 1) {
      return false
    }

    val state_it = Q.iterator()

    while( state_it.hasNext() ) {
      val state = state_it.next()
      val symbol2ins = inArcs(state)
      val inSymbols = symbol2ins.keySet()
      val inSymbolIt = inSymbols.iterator()
      while (inSymbolIt.hasNext()) {
        val symbol = inSymbolIt.next()
        val ins = symbol2ins(symbol)
        if (ins.size() > 1) {
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

  def addSymbol(m: Symbol): Unit = symbols.add(m)

  def iterator(): Iterator[Symbol] = symbols.iterator()
}

class CanonicalAcceptorFactory {

  def makeCanonicalAcceptor(ℒ: Language): Acceptor = {
    // TODO !!!
    null
  }

}

class Expression(v: List[Symbol]) {

  def getSymbolIterator(): Iterator[Symbol] = v.iterator()

  def addSymbol(s: Symbol): Unit = v.add(s)

  def length() = v.size()

  def getHead(): Symbol = v.elementAt(0)

  def getTail(): Expression = {
    var v_copy = List[Symbol]()
    v_copy.addAll(v)
    v_copy.removeElementAt(0)
    new Expression(v_copy)
  }

  def equals(other: Expression): Boolean = {
    // TODO !!!
    false
  }

  override def toString(): String = "\"" + v.mkString(" ") + "\""

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

  def getℒ(): Language = ℒ
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
  override def toString(): String = "▦"
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
  
  override def toString(): String = "{" + sequences.mkString(", ") + "}"
  
}

class Learner(T: Text)
{
  var iterator = T.iterator()
  
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    // default implementation never guesses a Grammar
    null
  }
  
  def nextExpression(): Expression = iterator.next()
  
  def hasNextExpression(): Boolean = iterator.hasNext()
  
}

class MemorizingLearner(T: Text) extends Learner(T) {

  var runningGuess = new Language()
  
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    if( ! ( s instanceof ▦ ) ) {
      runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }
}

class Partition {
  def restrictTo(subset: Set): Partition = {
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

class Quotient(A: Acceptor, pi: Partition) {
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
  
  def addExpression(s: Expression): Unit = v.add(s)
  
  def length() = v.size()
  
  def isFor(ℒ: Language) = content().equals(ℒ)
  
  def content(): Language = {
    var ℒ = new Language()
    val it = iterator()
    while( it.hasNext() ) {
      val s = it.next()
      if( ! ( s instanceof ▦ ) ) {
        ℒ.addExpression(s)
      }
    }
    ℒ
  }
  
  def iterator() = v.iterator()
  
  override def toString(): String = "<" + v.mkString(", ") + ">"
  
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
    
    Console.println("Text T = " + T )
    Console.println("Language ℒ = " + ℒ )
    Console.println()
    
    if( T.isFor(ℒ) ) {
      Console.println("T is for ℒ")
    }
    else {
      Console.println("T is not for ℒ")
    }
    Console.println()
    
    var ɸ = new MemorizingLearner(T)
    
    var guess: Grammar = null
    
    while( ɸ.hasNextExpression() ) {
      guess = ɸ.processNextExpression()
      if( guess != null ) {
        Language guessedLanguage = guess.ℒ()
        Console.println("ɸ.processNextExpression().ℒ = " + guessedLanguage )
        if( guessedLanguage.equals(ℒ) ) {
          Console.println("ɸ identified the language using the text")
          System.exit(0)
        }
        else {
          Console.println("ɸ's guess was not correct\n")
        }
      }
    }
    
    if ( guess == null ) {
      Console.println("ɸ never made a guess");
    }
    
  }
  
}
