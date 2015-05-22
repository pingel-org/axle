package axle.nlp

import org.specs2.mutable._
import spire.algebra.MetricSpace
import axle.algebra._
import axle.jblas._

class DocumentVectorSpaceSpec extends Specification {

  val stopwords = Set("the", "a", "of", "for", "in").toSet // TODO extend this

  val corpus = Vector(
    "the quick brown fox jumps over the lazy dog",
    "fox jumps over dog eden was the sumerian word edine",
    "hostname and fqdn for the instance can be critical if you're automating deployment with",
    "quick lazy word",
    "foo bar dog")

  "dvs" should {
    "work" in {

      val unweightedSpace = UnweightedDocumentVectorSpace(stopwords, corpus)
      implicit val space = unweightedSpace.space
      val vectors = corpus.map(unweightedSpace.doc2vector)

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      val unweightedDistanceMatrix = DistanceMatrix(vectors)

      1 must be equalTo 1
    }

    "work again" in {

      val lines = Vector("foo bar baz", "foo fu", "fu fu fu bar")
      val dvs = UnweightedDocumentVectorSpace(Set("this", "the"), lines)
      dvs.wordCount(lines) must be equalTo Map("foo" -> 2, "bar" -> 2, "baz" -> 1, "fu" -> 4)
      dvs.wordExistsCount(lines) must be equalTo Map("foo" -> 2, "bar" -> 2, "baz" -> 1, "fu" -> 2)
    }
  }

  "tfidf" should {
    "work" in {

      val tfidfSpace = TFIDFDocumentVectorSpace(stopwords, corpus)
      implicit val space = tfidfSpace.space
      val vectors = corpus.map(tfidfSpace.doc2vector)

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      val tfidfDistanceMatrix = DistanceMatrix(vectors)

      1 must be equalTo 1
    }
  }

}
