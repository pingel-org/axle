package axle.lx

import math.{ sqrt, log }
import spire.algebra._
import spire.math._
import spire.implicits._

class TFIDFDocumentVectorSpace(_stopwords: Set[String], corpusIterator: () => Iterator[String])
  extends DocumentVectorSpace {

  lazy val numDocs = corpusIterator().size // TODO expensive
  lazy val _vectors = corpusIterator().map(doc2vector(_))
  lazy val documentFrequency = mrWordExistsCount(corpusIterator()).withDefaultValue(1)

  def vectors() = _vectors

  def stopwords() = _stopwords

  def termWeight(term: String, doc: TermVector) =
    doc(term) * log(numDocs / documentFrequency(term).toDouble)

  def dotProduct(v1: TermVector, v2: TermVector) =
    (v1.keySet intersect v2.keySet).toList.map(term => termWeight(term, v1) * termWeight(term, v2)).sum

  def length(v: TermVector) =
    sqrt(v.map({ case (term, c) => termWeight(term, v) ** 2 }).sum)

  def space() = new MetricSpace[TermVector, Real] {

    def distance(v1: TermVector, v2: TermVector): Real =
      1 - dotProduct(v1, v2) / (length(v1) * length(v2))

  }

}
