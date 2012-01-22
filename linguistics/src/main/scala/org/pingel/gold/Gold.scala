
package org.pingel.gold

import scala.collection._

class Expression {
  
  var v = List[Morpheme]()

  def addMorpheme(m: Morpheme): Unit = {
	  v = v ::: List(m) // TODO: slow
  }
  
  def length = v.size

  override def toString() = "\"" + v.mkString(" ") + "\""
}

class ExpressionComparator extends Comparable[Expression] {
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

  var sequences = Set[Expression]() // TODO: was TreeSet using new ExpressionComparator()
  
  def addExpression(s: Expression): Unit = sequences += s
  
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
  
  def nextExpression() = iterator.next()
  
  def hasNextExpression() = iterator.hasNext
}

class MemorizingLearner(T: Text) extends Learner(T) {

  def runningGuess = new Language()

  def processNextExpression(): Grammar = {
    val s = nextExpression()
    s match {
      case _: ▦ => 
      case _ => runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }

}

class Morpheme(s: String, vocabulary: Vocabulary) {

  vocabulary.addMorpheme(this)

  def toString() = s
}

class Text {

  var v = mutable.ListBuffer[Expression]()
  
  def addExpression(s: Expression) = {
    v += s
  }
  
  def length() = v.size
  
  def isFor(ℒ: Language) = content().equals(ℒ)
  
  def content() = {

    var ℒ = new Language()
    for( s <- v ) {
      s match {
        case _: ▦ => 
        case _ => ℒ.addExpression(s)
      }
    }
    ℒ
  }
  
  def iterator() = v.iterator
  
  override def toString() = "<" + v.mkString(", ") + ">"
  
}

class Vocabulary {

  var morphemes = Set[Morpheme]()

  def addMorpheme(m: Morpheme) = {
    morphemes += m
  }

  def iterator() = morphemes.iterator
}
