package axle.nlp

import spire.algebra._
import spire.math._
import spire.implicits._

trait DocumentVectorSpace {

  type TermVector = Map[String, Int]

  val whitespace = """\s+""".r

  def stopwords: Set[String]

  val emptyCount = Map.empty[String, Int].withDefaultValue(0)

  def countWordsInLine(line: String): Map[String, Int] =
    whitespace.split(line.toLowerCase)
      .filter(!stopwords.contains(_))
      .aggregate(emptyCount)((m, w) => m + (w -> (m(w) + 1)), _ + _)

  def uniqueWordsInLine(line: String): Map[String, Int] =
    whitespace.split(line.toLowerCase)
      .filter(!stopwords.contains(_))
      .toSet
      .map((w: String) => (w, 1))
      .toMap

  def wordCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(emptyCount)((m, line) => m + countWordsInLine(line), _ + _)

  def wordExistsCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(emptyCount)((m, line) => m + uniqueWordsInLine(line), _ + _)

  def doc2vector(doc: String): TermVector = wordCount(List(doc))

  def space: MetricSpace[TermVector, Double]

}
