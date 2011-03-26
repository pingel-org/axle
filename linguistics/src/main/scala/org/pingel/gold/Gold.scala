
package org.pingel.gold

class Expression {
  var v = List[Morpheme]()

  def addMorpheme(m: Morpheme): Unit = v.add(v)
  
  def length(): Int = v.size()

  override def toString() = "\"" + v.mkString(" ") + "\""
}

class ExpressionComparator extends Comparator[Expression] {
  def compare(e1: Expression, e2: Expression): Int = e1.toString().compareTo(e2.toString())
}

trait Grammar {
  def L(): Language
}

class HardCodedGrammar(L: Language) extends Grammar {
  def getL() = L
}

class HardCodedLearner(T: Text, G: Grammar) extends Learner(T)
{
  def processNextExpression(): Grammar = {
    val s = nextExpression()
    G
  }
}

class ▦ extends Expression {
  // should this class throw an exception
  // if addMorpheme is called?
  def toString()  = "▦"
}

class Language {

  var sequences = new TreeSet[Expression](new ExpressionComparator())
  
  def addExpression(s: Expression): Unit = sequences.add(s)
  
  def equals(other: Language) = sequences.equals(other.sequences)
  
  override def toString() = "{" + sequences.mkString(", ") + "}"
}

class Learner(T: Text) {

  var iterator = T.iterator()
  
  def processNextExpression(): Grammar =  {
    val s = nextExpression()
    // default implementation never guesses a Grammar
    null
  }
  
  def nextExpression(): Expression = iterator.next()
  
  def hasNextExpression() = iterator.hasNext()
}

class MemorizingLearner(T: Text) extends Learner(T) {

  def runningGuess = new Language()

  def processNextExpression(): Grammar = {
    val s = nextExpression()
    if( ! ( s instanceof ▦ ) ) {
      runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }

}

class Morpheme(s: String, vocabulary: Vocabulary) {

  vocabulary.addMorpheme(this)

  def toString() = s
}

class Text {

  var v = List[Expression]()
  
  def addExpression(s: Expression) = v.add(s)
  
  def length(): Int = v.size()
  
  def isFor(L: Language) = content().equals(L)
  
  def content(): Language = {

    var L = new Language()
    for( s <- v ) {
      if( ! ( s instanceof ▦ ) ) {
        L.addExpression(s)
      }
    }
    L
  }
  
  def iterator() = v.iterator()
  
  override def toString() = "<" + v.mkString(", ") + ">"
  
}

class Vocabulary {

  var morphemes = Set[Morpheme]()

  def addMorpheme(m: Morpheme): Unit = morphemes.add(m)

  def iterator() = morphemes.iterator
}

object test {

  def main(args: List[String]): Unit = {

    var Σ = new Vocabulary()

    val mHi = new Morpheme("hi", Σ)
    val mIm = new Morpheme("I'm", Σ)
    val mYour = new Morpheme("your", Σ)
    val mMother = new Morpheme("Mother", Σ)
    val mShut = new Morpheme("shut", Σ)
    val mUp = new Morpheme("up", Σ)

    val s1 = new Expression()
    s1.addMorpheme(mHi)
    s1.addMorpheme(mIm)
    s1.addMorpheme(mYour)
    s1.addMorpheme(mMother)

    val s2 = new Expression()
    s2.addMorpheme(mShut)
    s2.addMorpheme(mUp)

    var L = new Language()
    L.addExpression(s1)
    L.addExpression(s2)

    var T = new Text()
    T.addExpression(s1)
    T.addExpression(new ▦())
    T.addExpression(new ▦())
    T.addExpression(s2)
    T.addExpression(new ▦())
    T.addExpression(s2)
    T.addExpression(s2)

    Console.println("Text T = " + T )
    Console.println("Language L = " + L )
    Console.println()
    
    if( T.isFor(L) ) {
      Console.println("T is for L")
    }
    else {
      Console.println("T is not for L")
    }
    Console.println()

    var ɸ = new MemorizingLearner(T)
    var guess: Grammar = null
    while( ɸ.hasNextExpression() ) {
      guess = ɸ.processNextExpression()
      if( guess != null ) {
	var guessedLanguage = guess.L()
	Console.println("ɸ.processNextExpression().L = " + guessedLanguage)
	if( guessedLanguage.equals(L) ) {
	  Console.println("ɸ identified the language using the text")
	  System.exit(0)
	}
	else {
	  Console.println("ɸ's guess was not correct\n")
	}
      }
    }
    if ( guess == null ) {
      Console.println("ɸ never made a guess")
    }
  }

}
