package axle.nlp

import spire.algebra._
import spire.math._
import spire.implicits._

case class TermVectorizer[C: Ring](stopwords: Set[String])
    extends Function1[String, Map[String, C]] {

  val one = Ring[C].one
  val zero = Ring[C].zero

  val whitespace = """\s+""".r

  val emptyCount = Map.empty[String, C].withDefaultValue(zero)

  def countWordsInLine(line: String): Map[String, C] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .aggregate(emptyCount)((m, w) => m + (w -> (m(w) + one)), _ + _)

  def uniqueWordsInLine(line: String): Map[String, C] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .toSet
      .map((w: String) => (w, one))
      .toMap

  def wordCount(is: Seq[String]): Map[String, C] =
    is.aggregate(emptyCount)((m, line) => m + countWordsInLine(line), _ + _)

  def wordExistsCount(is: Seq[String]): Map[String, C] =
    is.aggregate(emptyCount)((m, line) => m + uniqueWordsInLine(line), _ + _)

  def apply(doc: String): Map[String, C] =
    wordCount(List(doc))

}