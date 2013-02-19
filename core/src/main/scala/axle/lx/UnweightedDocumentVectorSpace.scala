package axle.lx

import spire.math._
import spire.implicits._
import spire.algebra._

class UnweightedDocumentVectorSpace(_stopwords: Set[String], corpusIterator: () => Iterator[String])
  extends DocumentVectorSpace {

  lazy val _vectors = corpusIterator().map(doc2vector(_)).toIndexedSeq

  def vectors() = _vectors

  def stopwords() = _stopwords

  /**
   *
   * distance(v1: TermVector, v2: TermVector): Real = 1 - dot(v1, v2) / (norm(v1) * norm(v2))
   *
   */

  val _space = new NormedInnerProductSpace[TermVector, Real] {

    def nroot = NRoot.RealIsNRoot

    val _innerProductSpace = new InnerProductSpace[TermVector, Real] {

      def negate(x: TermVector) = x.map(kv => (kv._1, -1 * kv._2)) // Not sure this makes much sense

      def zero = Map()

      def plus(x: TermVector, y: TermVector) =
        (x.keySet union y.keySet).toIterable.map(k => (k, x.get(k).getOrElse(0) + y.get(k).getOrElse(0))).toMap

      def timesl(r: Real, v: TermVector) = v.map(kv => (kv._1, (kv._2 * r).toInt))

      implicit def scalar = Field.RealIsField

      def dot(v1: TermVector, v2: TermVector) =
        (v1.keySet intersect v2.keySet).toList.map(w => v1(w) * v2(w)).sum
    }

    def space() = _innerProductSpace
  }

  def space() = _space

}

