package axle.nlp

import spire.algebra.Ring
import spire.implicits.MapRng
import spire.implicits.additiveSemigroupOps

case class TermVectorizer[V: Ring](stopwords: Set[String])
    extends Function1[String, Map[String, V]] {

  val one = Ring[V].one
  val zero = Ring[V].zero

  val whitespace = """\s+""".r

  val emptyCount = Map.empty[String, V].withDefaultValue(zero)

  def countWordsInLine(line: String): Map[String, V] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .aggregate(emptyCount)((m, w) => m + (w -> (m(w) + one)), _ + _)

  def uniqueWordsInLine(line: String): Map[String, V] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .toSet
      .map((w: String) => (w, one))
      .toMap

  def wordCount(is: Seq[String]): Map[String, V] =
    is.aggregate(emptyCount)((m, line) => m + countWordsInLine(line), _ + _)

  def wordExistsCount(is: Seq[String]): Map[String, V] =
    is.aggregate(emptyCount)((m, line) => m + uniqueWordsInLine(line), _ + _)

  def apply(doc: String): Map[String, V] =
    wordCount(List(doc))

}