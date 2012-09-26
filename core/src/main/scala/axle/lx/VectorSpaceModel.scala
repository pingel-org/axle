package axle.lx

import math.sqrt
import axle._
import axle.matrix.JblasMatrixFactory._

/**
 * Vector Space Model
 *
 * For calculating document similarity
 *
 * http://en.wikipedia.org/wiki/Vector_space_model
 *
 */

object TermVector {

  type TV = Map[String, Int]

  def length(q: TV): Double = sqrt(q.map({ case (w, c) => c * c }).sum)

  def cosθ(d: TV, q: TV): Double = (d.keySet intersect q.keySet).toList.map(w => d(w) * q(w)).sum / (length(d) * length(q))

}

object VectorSpaceModel {

  // TODO: tfidf weights
  // TODO: edit distance tolerance

  import TermVector._

  val whitespace = """\s+""".r

  val stopwords = Set("the", "a", "of", "for", "in") // TODO: extend this

  // TODO: stemming
  def doc2vector(doc: String): TV =
    ScalaMapReduce.mapReduce(
      List(doc).iterator,
      mapper = (doc: String) => whitespace.split(doc).filter(!stopwords.contains(_)).map((_, 1)),
      reducer = (v1: Int, v2: Int) => v1 + v2
    )

  def similarityMatrix(vectors: List[TV]): Matrix[Double] = {
    val n = vectors.size
    val result = zeros[Double](n, n)
    (0 until n).doubles.map({ case (r, c) => result(r, c) = cosθ(vectors(r), vectors(c)) })
    result
  }

  def makeDictionary(corpus: List[String]): IndexedSeq[String] =
    corpus.map(whitespace.split(_).toSet).reduce(_ ++ _).filter(!stopwords.contains(_)).toIndexedSeq.sorted

  def processCorpus(corpus: List[String]): (List[TV], IndexedSeq[String]) =
    (corpus.map(doc2vector(_)), makeDictionary(corpus))

}

object VSMDemo {

  import VectorSpaceModel._

  val corpus = List(
    "the quick brown fox jumps over the lazy dog",
    "fox jumps over dog eden was the sumerian word edine",
    "quick lazy word",
    "foo bar dog")

  val (vectors, dictionary) = processCorpus(corpus)

  similarityMatrix(vectors)

}