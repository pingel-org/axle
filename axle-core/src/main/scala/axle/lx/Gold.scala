
package axle.lx

import scala.language.implicitConversions
import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

class GoldParadigmLearner[S](
  initialState: S,
  learnFrom: (S, GoldParadigm.Expression) => (S, Option[GoldParadigm.Grammar])
) {

    def guesses(T: GoldParadigm.Text): Iterator[GoldParadigm.Grammar] =
      T.expressions
        .iterator
        .scanLeft((initialState, GoldParadigm.noGuess))((sg, e) => learnFrom(sg._1, e))
        .flatMap(_._2)
  }

object GoldParadigm {

  val noGuess = Option.empty[Grammar]

  type Expression = Iterable[Morpheme]

  object Expression {

    implicit val showExpression: Show[Expression] =
      _.mkString(" ")

    implicit val orderExpression: Order[Expression] =
      (x, y) => x.show.compareTo(y.show)

  }

  val ♯ = List.empty[Morpheme]

  trait Grammar {
    def ℒ: Language
  }

  case class HardCodedGrammar(ℒ: Language) extends Grammar

  implicit def enLanguage(sequences: Set[Expression]): Language = Language(sequences)

  case class Language(sequences: Set[Expression])

  object Language {

    implicit def showLanguage: Show[Language] =
      l => "{" + l.sequences.mkString(", ") + "}"

    implicit val languageEq: Eq[Language] =
      (x, y) => x.sequences.equals(y.sequences)
  }

  def hardCodedLearner(G: Grammar) = new GoldParadigmLearner[Unit](
    (),
    (state: Unit, e: Expression) => ((), Some(G))
  )

  val memorizingLearner = new GoldParadigmLearner[Language](
    Language(Set.empty),
    (state: Language, expression: Expression) => {
      val newState = 
        if( expression.size > 0 ) {
          Language(state.sequences ++ List(expression))
        } else {
          state
        }
      (newState, Some(HardCodedGrammar(newState)))
    })

  case class Morpheme(s: String)
  object Morpheme {

    implicit def showMorpheme: Show[Morpheme] = _.s
  }

  case class Text(expressions: List[Expression]) {

    val length: Int = expressions.size

    def isFor(ℒ: Language) = content.equals(ℒ) // TODO equals

    val content: Language = Language(expressions.filter(_ != ♯).toSet)
  }
  object Text {

    implicit def showText: Show[Text] =
      t => "<" + t.expressions.mkString(", ") + ">"
  }

  implicit def enVocabulary(morphemes: Set[Morpheme]): Vocabulary = Vocabulary(morphemes)

  case class Vocabulary(morphemes: Set[Morpheme]) {

    def iterator: Iterator[Morpheme] = morphemes.iterator
  }

}
