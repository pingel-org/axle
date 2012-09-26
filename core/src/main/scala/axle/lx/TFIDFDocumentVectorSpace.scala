package axle.lx

class TFIDFDocumentVectorSpace(_stopwords: Set[String], corpus: List[String]) extends DocumentVectorSpace {

  import math.{ sqrt, log }

  lazy val numDocs = corpus.size
  lazy val _vectors = corpus.map(doc2vector(_)).toIndexedSeq
  lazy val df = mrWordExistsCount(corpus.iterator)

  def vectors() = _vectors

  def stopwords() = _stopwords

  def termWeight(term: String, doc: TV): Double = doc(term) * log(numDocs / df(term).toDouble)

  def dotProduct(v1: TV, v2: TV): Double =
    (v1.keySet intersect v2.keySet).toList.map(term => termWeight(term, v1) * termWeight(term, v2)).sum

  def length(v: TV): Double = sqrt(v.map({ case (term, c) => square(termWeight(term, v)) }).sum)

  def similarity(v1: TV, v2: TV): Double = dotProduct(v1, v2) / (length(v1) * length(v2))

}
