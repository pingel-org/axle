package axle.nlp

/**
 * Vector Space Model
 *
 * For calculating document similarity.
 *
 * Note that distance and similarity are inverses
 *
 * http://en.wikipedia.org/wiki/Vector_space_model
 */

// TODO edit distance tolerance
// TODO stemming

import axle._
import spire.algebra.MetricSpace
import spire.implicits._
import spire.algebra._
import spire.math._

trait DocumentVectorSpace {

  import ScalaMapReduce.mapReduce

  type TermVector = Map[String, Int]

  val whitespace = """\s+""".r

  def stopwords(): Set[String]

  def countWordsInLine(line: String): Map[String, Int] =
    whitespace.split(line.toLowerCase)
      .filter(!stopwords.contains(_))
      .foldLeft(Map.empty[String, Int].withDefaultValue(0))({ case (m, w) => m + (w -> (m(w) + 1)) })

  def uniqueWordsInLine(line: String): Map[String, Int] =
    whitespace.split(line.toLowerCase)
      .filter(!stopwords.contains(_))
      .toSet
      .foldLeft(Map.empty[String, Int].withDefaultValue(0))({ case (m, w) => m + (w -> (m(w) + 1)) })

  implicit val intsemi = axle.algebra.Semigroups.IntSemigroup
      
  def wordCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(Map.empty[String, Int].withDefaultValue(0)
    )((m, line) => m |+| countWordsInLine(line), _ |+| _)

  def wordExistsCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(Map.empty[String, Int].withDefaultValue(0)
    )((m, line) => m |+| uniqueWordsInLine(line), _ |+| _)

  def doc2vector(doc: String): TermVector = wordCount(List(doc))

  def space(): MetricSpace[TermVector, Double]

}
