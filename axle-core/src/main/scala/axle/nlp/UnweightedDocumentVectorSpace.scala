package axle.nlp

import spire.math._
import spire.implicits._
import spire.algebra._

class UnweightedDocumentVectorSpace(
  _stopwords: Set[String],
  corpusIterator: () => Iterator[String])
  extends DocumentVectorSpace {

  lazy val _vectors = corpusIterator().map(doc2vector).toIndexedSeq

  def vectors: IndexedSeq[TermVector] = _vectors

  def stopwords: Set[String] = _stopwords

  /**
   *
   * distance(v1: TermVector, v2: TermVector) = 1 - dot(v1, v2) / (norm(v1) * norm(v2))
   *
   */

  val _space = (new InnerProductSpace[TermVector, Double] {

    def negate(x: TermVector): TermVector = x.map(kv => (kv._1, -1 * kv._2)) // Not sure this makes much sense

    def zero: TermVector = Map()

    def plus(x: TermVector, y: TermVector): TermVector =
      (x.keySet union y.keySet).toIterable.map(k => (k, x.get(k).getOrElse(0) + y.get(k).getOrElse(0))).toMap

    def timesl(r: Double, v: TermVector): TermVector = v.map(kv => (kv._1, (kv._2 * r).toInt))

    def scalar: Field[Double] = DoubleAlgebra

    def dot(v1: TermVector, v2: TermVector): Double =
      (v1.keySet intersect v2.keySet).toList.map(w => v1(w) * v2(w)).sum

  }).normed

  def space: MetricSpace[TermVector, Double] = _space

}

