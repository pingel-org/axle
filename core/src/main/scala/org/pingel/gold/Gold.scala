
package org.pingel.gold

import scala.collection._

case class Expression(v: List[Morpheme]) {

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
  override def processExpression(e: Expression): Grammar = {
    val s = e
    G
  }
}

object ▦ extends ▦

class ▦() extends Expression(Nil) {
  // should this class throw an exception
  // if addMorpheme is called?
  override def toString() = "▦"
}

case class Language {

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

  def processExpression(e: Expression): Grammar = {
    val s = e
    // default implementation never guesses a Grammar
    null
  }

  def learn(correct: Grammar => Boolean): Option[Grammar] = {
    val it = T.iterator
    while (it.hasNext) {
      val e = it.next()
      val guess = processExpression(e)
      if (guess != null) {
        if (correct(guess)) {
          return Some(guess)
        }
      }
    }
    None
  }

}

case class MemorizingLearner(T: Text) extends Learner(T) {

  def runningGuess = new Language()

  override def processExpression(e: Expression): Grammar = {
    e match {
      case _: ▦ =>
      case _ => runningGuess.addExpression(e)
    }
    new HardCodedGrammar(runningGuess)
  }

}

case class Morpheme(s: String) {
  override def toString() = s
}

case class Text(v: List[Expression]) {

  def length() = v.size

  def isFor(ℒ: Language) = content().equals(ℒ)

  def content() = {

    val ℒ = new Language()
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

case class Vocabulary {

  var morphemes = Set[Morpheme]()

  def morpheme(s: String) = {
    val m = new Morpheme(s)
    morphemes += m
    m
  }

  def iterator() = morphemes.iterator
}
