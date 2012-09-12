
package axle.lx

import collection._

object Gold {

  case class Expression(v: List[Morpheme]) {

    def length = v.size

    override def toString() = "\"" + v.mkString(" ") + "\""
  }

  class ExpressionComparator extends Comparable[Expression] {
    def compareTo(other: Expression): Int = this.toString().compareTo(other.toString())
  }

  trait Grammar {
    def L(): Language
  }

  class HardCodedGrammar(L: Language) extends Grammar {
    def L() = L
  }

  class HardCodedLearner(T: Text, G: Grammar) extends Learner(T) {
    override def processExpression(e: Expression): Option[Grammar] = {
      val s = e
      Some(G)
    }
  }

  object ▦ extends ▦

  class ▦() extends Expression(Nil) {
    // should this class throw an exception
    // if addMorpheme is called?
    override def toString() = "▦"
  }

  case class Language() {

    val sequences = mutable.Set[Expression]() // TODO: was TreeSet using new ExpressionComparator()

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

    def processExpression(e: Expression): Option[Grammar] = {
      val s = e
      // default implementation never guesses a Grammar
      None
    }

    def learn(correct: Grammar => Boolean): Option[Grammar] =
      T.expressions.iterator
        .map(processExpression(_).filter(correct(_)))
        .find(_.isDefined)
        .getOrElse(None)

  }

  case class MemorizingLearner(T: Text) extends Learner(T) {

    def runningGuess = new Language()

    override def processExpression(e: Expression): Option[Grammar] = {
      e match {
        case _: ▦ =>
        case _ => runningGuess.addExpression(e)
      }
      Some(new HardCodedGrammar(runningGuess))
    }

  }

  case class Morpheme(s: String) {
    override def toString() = s
  }

  case class Text(expressions: List[Expression]) {

    def length() = expressions.size

    def isFor(ℒ: Language) = content().equals(ℒ)

    def content() = {

      val ℒ = new Language()
      for (s <- expressions) {
        s match {
          case _: ▦ =>
          case _ => ℒ.addExpression(s)
        }
      }
      ℒ
    }

    override def toString() = "<" + expressions.mkString(", ") + ">"

  }

  case class Vocabulary() {

    val morphemes = mutable.Set[Morpheme]()

    def morpheme(s: String) = {
      val m = new Morpheme(s)
      morphemes += m
      m
    }

    def iterator() = morphemes.iterator
  }

}