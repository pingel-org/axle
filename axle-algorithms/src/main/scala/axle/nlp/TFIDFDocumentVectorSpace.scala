package axle.nlp

import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra

/**
 *
 * Note:
 *
 * norm(v) = sqrt(dot(v, v))
 *
 * distance(v1, v2) = 1 - dot(v1, v2) / (norm(v1) * norm(v2))
 */

object TFIDFDocumentVectorSpace {

  /**
   *
   * distance(v1: TermVector, v2: TermVector) = 1 - dot(v1, v2) / (norm(v1) * norm(v2))
   *
   */

  def apply(_stopwords: Set[String],
    corpusIterable: Iterable[String]): DocumentVectorSpace =
    new DocumentVectorSpace {

      def stopwords: Set[String] = _stopwords

      lazy val numDocs = corpusIterable.iterator.size // TODO expensive

      lazy val _vectors = corpusIterable.iterator.map(doc2vector).toIndexedSeq

      def documentFrequency = wordExistsCount(corpusIterable.iterator.toList).withDefaultValue(1)

      val innerProductSpace = new InnerProductSpace[TermVector, Double] {

        def negate(x: TermVector): TermVector = x.map(kv => (kv._1, -1 * kv._2)) // Not sure this makes much sense

        def zero: TermVector = Map()

        def plus(x: TermVector, y: TermVector): TermVector =
          (x.keySet union y.keySet).toIterable.map(k => (k, x.get(k).getOrElse(0) + y.get(k).getOrElse(0))).toMap

        def timesl(r: Double, v: TermVector): TermVector = v.map(kv => (kv._1, (kv._2 * r).toInt))

        def scalar: Field[Double] = DoubleAlgebra

        private[this] def termWeight(term: String, doc: TermVector) =
          doc(term) * math.log(numDocs / documentFrequency(term).toDouble)

        def dot(v1: TermVector, v2: TermVector): Double =
          (v1.keySet intersect v2.keySet).toList.map(term => termWeight(term, v1) * termWeight(term, v2)).sum

      }

      def space: MetricSpace[TermVector, Double] = innerProductSpace.normed
    }

}
