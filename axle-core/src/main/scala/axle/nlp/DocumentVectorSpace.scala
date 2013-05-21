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
import spire.math._
import spire.algebra.MetricSpace
import collection._

trait DocumentVectorSpace {

  import ScalaMapReduce.mapReduce

  type TermVector = Map[String, Int]

  val whitespace = """\s+""".r

  def stopwords(): Set[String]

  def mrWordCount(is: Seq[String]): Map[String, Int] =
    is.foldLeft(immutable.Map.empty[String, Int].withDefaultValue(0))({
      case (tally, doc) =>
        whitespace
          .split(doc.toLowerCase)
          .filter(!stopwords.contains(_))
          .foldLeft(tally)({ case (tally, word) => tally + (word -> (tally(word) + 1)) })
    })

  def mrWordExistsCount(is: Seq[String]): Map[String, Int] =
    is.foldLeft(immutable.Map.empty[String, Int].withDefaultValue(0))({
      case (tally, doc) =>
        whitespace
          .split(doc.toLowerCase)
          .toSet
          .filter(!stopwords.contains(_))
          .foldLeft(tally)({ case (tally, word) => tally + (word -> (tally(word) + 1)) })
    })

  def doc2vector(doc: String): TermVector = mrWordCount(List(doc))

  def space(): MetricSpace[TermVector, Double]

}
