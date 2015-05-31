package axle.nlp

import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra

case class UnweightedDocumentVectorSpace(
  corpusIterable: Iterable[String], termVectorizer: TermVectorizer)
    extends DocumentVectorSpace {

  val vectors = corpusIterable.iterator.map(termVectorizer).toIndexedSeq

  def dot(v1: Map[String, Int], v2: Map[String, Int]): Double =
    (v1.keySet intersect v2.keySet).toList.map(w => v1(w) * v2(w)).sum

}

