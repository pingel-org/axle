
package axle.lx

import collection._

object Gold {

  type Expression = Iterable[Morpheme]

  val ♯ = List[Morpheme]()

  class ExpressionComparator extends Comparable[Expression] {
    def compareTo(other: Expression): Int = this.toString().compareTo(other.toString())
  }

  trait Grammar {
    def ℒ(): Language
  }

  class HardCodedGrammar(ℒ: Language) extends Grammar {
    def ℒ() = ℒ
  }

  implicit def enLanguage(sequences: Set[Expression]): Language = Language(sequences)

  case class Language(sequences: Set[Expression]) {

    def equals(other: Language) = sequences.equals(other.sequences)

    override def toString() = "{" + sequences.mkString(", ") + "}"
  }

  trait Learner[S] {

    def initialState(): S

    def processExpression(state: S, expression: Expression): (S, Option[Grammar])

    val noGuess = None.asInstanceOf[Option[Grammar]]

    def guesses(T: Text): Iterator[Grammar] =
      T.expressions.iterator
        .scanLeft((initialState(), noGuess))((sg, e) => processExpression(sg._1, e))
        .flatMap(_._2)
  }

  case class HardCodedLearner(G: Grammar) extends Learner[Nothing] {

    def initialState() = null.asInstanceOf[Nothing]

    def processExpression(state: Nothing, e: Expression) = (initialState(), Some(G))
  }

  case class MemorizingLearner() extends Learner[Language] {

    def initialState() = Language(Set())
    
    def processExpression(state: Language, expression: Expression) = {
      val newState = Language(state.sequences ++ List(expression))
      (newState, Some(new HardCodedGrammar(newState)))
    }

  }

  case class Morpheme(s: String) {
    override def toString() = s
  }

  case class Text(expressions: List[Expression]) {

    def length() = expressions.size

    def isFor(ℒ: Language) = content().equals(ℒ) // TODO equals

    def content() = new Language(expressions.filter(_ != ♯).toSet)

    override def toString() = "<" + expressions.mkString(", ") + ">"
  }

  implicit def enVocabulary(morphemes: Set[Morpheme]): Vocabulary = Vocabulary(morphemes)

  case class Vocabulary(morphemes: Set[Morpheme]) {

    def iterator() = morphemes.iterator
  }

}
