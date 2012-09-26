package axle.lx

/**
 * Vector Space Model
 *
 * For calculating document similarity
 *
 * http://en.wikipedia.org/wiki/Vector_space_model
 */

// TODO edit distance tolerance
// TODO stemming

trait DocumentVectorSpace {

  import axle._
  import axle.matrix.JblasMatrixFactory._

  def square(x: Double): Double = x * x

  type TV = Map[String, Int]

  def mrWordCount(is: Iterator[String]): Map[String, Int] =
    ScalaMapReduce.mapReduce(
      is,
      mapper = (doc: String) => whitespace.split(doc).filter(!stopwords.contains(_)).map((_, 1)),
      reducer = (v1: Int, v2: Int) => v1 + v2
    )

  def mrWordExistsCount(is: Iterator[String]): Map[String, Int] =
    ScalaMapReduce.mapReduce(
      is,
      mapper = (doc: String) => whitespace.split(doc).toSet.filter(!stopwords.contains(_)).toList.map((_, 1)),
      reducer = (v1: Int, v2: Int) => v1 + v2
    )

  val whitespace = """\s+""".r

  def doc2vector(doc: String): TV = mrWordCount(List(doc).iterator)

  def stopwords(): Set[String]

  def dotProduct(d: TV, q: TV): Double

  def length(q: TV): Double

  def similarity(d: TV, q: TV): Double

  def vectors(): IndexedSeq[TV]

  def similarityMatrix(): Matrix[Double] = {
    val n = vectors.size
    val vs = vectors()
    val result = zeros[Double](n, n)
    for (
      r <- 0 until n;
      c <- 0 until n
    ) yield { result(r, c) = similarity(vs(r), vs(c)) }
    result
  }
}
