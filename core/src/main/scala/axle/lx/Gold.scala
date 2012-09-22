
package axle.lx

import collection._

object Gold {

  type Expression = List[Morpheme]

  val ▦ = List[Morpheme]()

  class ExpressionComparator extends Comparable[Expression] {
    def compareTo(other: Expression): Int = this.toString().compareTo(other.toString())
  }

  trait Grammar {
    def ℒ(): Language
  }

  class HardCodedGrammar(ℒ: Language) extends Grammar {
    def ℒ() = ℒ
  }

  class HardCodedLearner(T: Text, G: Grammar) extends Learner(T) {
    override def processExpression(e: Expression): Option[Grammar] = {
      val s = e
      Some(G)
    }
  }

  case class Language(_sequences: Set[Expression] = Set()) {

    val sequences = mutable.Set() ++ _sequences

    def expression(e: Expression): Expression = {
      sequences += e
      e
    }
    
    def equals(other: Language) = sequences.equals(other.sequences)

    override def toString() = "{" + sequences.mkString(", ") + "}"
  }

  class Learner(T: Text) {

    def processExpression(e: Expression): Option[Grammar] = {
      val s = e
      // default implementation never guesses a Grammar
      None
    }

    def guesses(): Iterator[Grammar] = T.expressions.iterator.flatMap(processExpression(_))

  }

  case class MemorizingLearner(T: Text) extends Learner(T) {

    var _runningGuess = new Language(Set())

    override def processExpression(e: Expression): Option[Grammar] = {
      _runningGuess = new Language(_runningGuess.sequences + e)
      Some(new HardCodedGrammar(_runningGuess))
    }

  }

  case class Morpheme(s: String) {
    override def toString() = s
  }

  case class Text(expressions: List[Expression]) {

    def length() = expressions.size

    def isFor(ℒ: Language) = content().equals(ℒ) // TODO equals

    def content() = new Language(expressions.filter(_ != ▦).toSet)

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