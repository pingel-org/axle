
package org.pingel.gold

import scala.collection._

class Expression(v: List[Morpheme]) {

  //  var v = List[Morpheme]()

  //  def addMorpheme(m: Morpheme): Unit = {
  //    v = v ::: List(m) // TODO: slow
  //  }

  def length = v.size

  override def toString() = "\"" + v.mkString(" ") + "\""
}

class ExpressionComparator extends Comparable[Expression] {
  def compareTo(other: Expression): Int = this.toString().compareTo(other.toString())
}

trait Grammar {
  def getL(): Language
}

class HardCodedGrammar(L: Language) extends Grammar {
  def getL() = L
}

class HardCodedLearner(T: Text, G: Grammar) extends Learner(T) {
  override def processNextExpression(): Grammar = {
    val s = nextExpression()
    G
  }
}

object ▦ extends ▦

class ▦() extends Expression(Nil) {
  // should this class throw an exception
  // if addMorpheme is called?
  override def toString() = "▦"
}

class Language {

  var sequences = Set[Expression]() // TODO: was TreeSet using new ExpressionComparator()

  def expression(ms: List[Morpheme]): Expression = {
    val expression = new Expression(ms)
    sequences += expression
    expression
  }

  def addExpression(e: Expression): Unit = sequences += e

  def equals(other: Language) = sequences.equals(other.sequences)

  override def toString() = "{" + sequences.mkString(", ") + "}"
}

class Learner(T: Text) {

  var iterator = T.iterator()

  def processNextExpression(): Grammar = {
    val s = nextExpression()
    // default implementation never guesses a Grammar
    null
  }

  def nextExpression() = iterator.next()

  def hasNextExpression() = iterator.hasNext
}

class MemorizingLearner(T: Text) extends Learner(T) {

  def runningGuess = new Language()

  override def processNextExpression(): Grammar = {
    val s = nextExpression()
    s match {
      case _: ▦ =>
      case _ => runningGuess.addExpression(s)
    }
    new HardCodedGrammar(runningGuess)
  }

}

class Morpheme(s: String) {
  override def toString() = s
}

case class Text(v: List[Expression]) {

  def length() = v.size

  def isFor(ℒ: Language) = content().equals(ℒ)

  def content() = {

    var ℒ = new Language()
    for (s <- v) {
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

  def morpheme(s: String) = {
    val m = new Morpheme(s)
    morphemes += m
    m
  }

  def iterator() = morphemes.iterator
}
