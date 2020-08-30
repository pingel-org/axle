package axle.nlp

import spire.algebra.CRing
import spire.implicits.additiveSemigroupOps

case class TermVectorizer[V: CRing](stopwords: Set[String])
  extends Function1[String, Map[String, V]] {

  val one = CRing[V].one
  val zero = CRing[V].zero

  val whitespace = """\s+""".r

  val emptyCount = Map.empty[String, V].withDefaultValue(zero)

  def countWordsInLine(line: String): Map[String, V] = {
    whitespace
      .split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .foldLeft(emptyCount)((m, w) => m + (w -> (m(w) + one)))
  }
  
  def uniqueWordsInLine(line: String): Map[String, V] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .toSet
      .map((w: String) => (w, one))
      .toMap

  val mapCR = new spire.std.MapCRng[String, V]()

  def wordCount(is: Seq[String]): Map[String, V] =
    is.foldLeft(emptyCount)((m, line) => mapCR.plus(m, countWordsInLine(line)))

  def wordExistsCount(is: Seq[String]): Map[String, V] =
    is.foldLeft(emptyCount)((m, line) => mapCR.plus(m, uniqueWordsInLine(line)))

  def apply(doc: String): Map[String, V] =
    wordCount(List(doc))

}
