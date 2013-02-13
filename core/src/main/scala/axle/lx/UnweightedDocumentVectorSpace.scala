package axle.lx

class UnweightedDocumentVectorSpace(_stopwords: Set[String], corpus: List[String]) extends DocumentVectorSpace {

  import math.sqrt

  lazy val _vectors = corpus.map(doc2vector(_)).toIndexedSeq

  def vectors() = _vectors

  def stopwords() = _stopwords

  def dotProduct(d: TV, q: TV): Double = (d.keySet intersect q.keySet).toList.map(w => d(w) * q(w)).sum

  def length(q: TV): Double = sqrt(q.map({ case (_, c) => square(c) }).sum)

  def similarity(d: TV, q: TV): Double = dotProduct(d, q) / (length(d) * length(q))

}

