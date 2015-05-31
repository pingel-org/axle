package axle.nlp

import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra

/**
 *
 *
 */

case class TFIDFDocumentVectorSpace(
  corpusIterable: Iterable[String], termVectorizer: TermVectorizer)
    extends DocumentVectorSpace {

  val numDocs = corpusIterable.iterator.size

  val vectors = corpusIterable.iterator.map(termVectorizer).toIndexedSeq

  val documentFrequency = termVectorizer.wordExistsCount(corpusIterable.iterator.toList).withDefaultValue(1)

  private[this] def termWeight(term: String, doc: Map[String, Int]) =
    doc(term) * math.log(numDocs / documentFrequency(term).toDouble)

  def dot(v1: Map[String, Int], v2: Map[String, Int]): Double =
    (v1.keySet intersect v2.keySet).toList.map(term => termWeight(term, v1) * termWeight(term, v2)).sum

}
