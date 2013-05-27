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
import spire.algebra._ // .MetricSpace
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
      .foldLeft(Map.empty[String, Int].withDefaultValue(0))({ case (m, w) => m + (w -> (m(w) + 1)) })

  // TODO: put this in MapSemigroup
  type V = Int // TODO: this becomes type param on map semigroup op
  def addMaps[K](x: Map[K, V], y: Map[K, V]): Map[K, V] =
    (x.keySet ++ y.keySet).map(k => {
      if (x.contains(k))
        if (y.contains(k)) (k, x(k) + y(k))
        else (k, x(k))
      else
        (k, y(k))
    }).toMap

  def wordCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(Map.empty[String, Int].withDefaultValue(0)
    )((m, line) => addMaps(m, countWordsInLine(line)), addMaps(_, _))

  def wordExistsCount(is: Seq[String]): Map[String, Int] =
    is.aggregate(Map.empty[String, Int].withDefaultValue(0)
    )((m, line) => addMaps(m, uniqueWordsInLine(line)), addMaps(_, _))

  def doc2vector(doc: String): TermVector = wordCount(List(doc))

  def space(): MetricSpace[TermVector, Double]

}
