package axle.lx

import spire.math._
import spire.implicits._
import spire.algebra.MetricSpace

class UnweightedDocumentVectorSpace(_stopwords: Set[String], corpusIterator: () => Iterator[String])
  extends DocumentVectorSpace {

  import math.sqrt

  lazy val _vectors = corpusIterator().map(doc2vector(_)).toIndexedSeq

  def vectors() = _vectors

  def stopwords() = _stopwords

  def dotProduct(v1: TermVector, v2: TermVector) =
    (v1.keySet intersect v2.keySet).toList.map(w => v1(w) * v2(w)).sum

  def length(v: TermVector): Double = sqrt(v.map({ case (_, c) => c ** 2 }).sum)

  def space() = new MetricSpace[TermVector, Real] {

    def distance(v1: TermVector, v2: TermVector): Real =
      1 - dotProduct(v1, v2) / (length(v1) * length(v2))

  }

}

