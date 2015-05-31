package axle.nlp

import org.specs2.mutable._
import spire.algebra.MetricSpace
import axle.algebra._
import axle.jblas._

class DocumentVectorSpaceSpec extends Specification {

  "TermVectorizer" should {
    "create term vectors correctly" in {

      val stopwords = Set("this", "the")
      import spire.implicits.DoubleAlgebra
      val vectorizer = TermVectorizer[Double](stopwords)

      val lines = Vector("foo bar baz", "foo fu", "fu fu fu bar")

      vectorizer.wordCount(lines) must be equalTo Map("foo" -> 2d, "bar" -> 2d, "baz" -> 1d, "fu" -> 4d)
      vectorizer.wordExistsCount(lines) must be equalTo Map("foo" -> 2d, "bar" -> 2d, "baz" -> 1d, "fu" -> 2d)
    }
  }

  val stopwords = Set("the", "a", "of", "for", "in").toSet // TODO extend this

  val corpus = Vector(
    "the quick brown fox jumps over the lazy dog",
    "fox jumps over dog eden was the sumerian word edine",
    "hostname and fqdn for the instance can be critical if you're automating deployment with",
    "quick lazy word",
    "foo bar dog")

  "dvs" should {
    "create a distance matrix on sample corpus" in {

      import spire.implicits.DoubleAlgebra
      val vectorizer = TermVectorizer[Double](stopwords)

      val unweightedSpace = UnweightedDocumentVectorSpace[Double]()

      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      implicit val normedUnweightedSpace = unweightedSpace.normed

      val unweightedDistanceMatrix = DistanceMatrix(corpus.map(vectorizer))

      1 must be equalTo 1
    }
  }

  "tfidf" should {
    "create a distance matrix on sample corpus" in {

      import spire.implicits.DoubleAlgebra
      val vectorizer = TermVectorizer[Double](stopwords)

      implicit val tfidfSpace = TFIDFDocumentVectorSpace[Double](corpus, vectorizer)
      val vectors = corpus.map(vectorizer)

      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      implicit val normedTfidf = tfidfSpace.normed
      val tfidfDistanceMatrix = DistanceMatrix(vectors)

      1 must be equalTo 1
    }
  }

}
