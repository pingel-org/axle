package axle.lx

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

trait DocumentVectorSpace {

  import ScalaMapReduce.mapReduce

  type TermVector = Map[String, Int]

  def doc2vector(doc: String): TermVector = mrWordCount(List(doc.toLowerCase).iterator).withDefaultValue(0)

  def dotProduct(d: TermVector, q: TermVector): Double

  def length(q: TermVector): Double

  def mrWordCount(is: Iterator[String]): Map[String, Int] = mapReduce(
    is,
    mapper = (doc: String) => whitespace.split(doc).filter(!stopwords.contains(_)).map((_, 1)),
    reducer = (v1: Int, v2: Int) => v1 + v2
  )

  def mrWordExistsCount(is: Iterator[String]): Map[String, Int] = mapReduce(
    is,
    mapper = (doc: String) => whitespace.split(doc).toSet.filter(!stopwords.contains(_)).toList.map((_, 1)),
    reducer = (v1: Int, v2: Int) => v1 + v2
  )

  val whitespace = """\s+""".r

  def stopwords(): Set[String]

  def space(): MetricSpace[TermVector, Real]

}
