package axle.nlp

import scala.Vector

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.DistanceMatrix
import cats.kernel.Eq
import spire.laws.VectorSpaceLaws
import spire.math.Real
import spire.implicits._
import axle.spireToCatsEq
// import axle.catsToSpireEq
import cats.implicits._

class DocumentVectorSpaceSpec
    extends Specification
    with Discipline {

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

  val stopwords = Set("the", "a", "of", "for", "in").toSet

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

      import axle.jblas.linearAlgebraDoubleMatrix
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      implicit val normedUnweightedSpace = unweightedSpace.normed

      val unweightedDistanceMatrix = DistanceMatrix(corpus.map(vectorizer))

      unweightedDistanceMatrix.distanceMatrix.get(2, 2) must be equalTo 0d
    }
  }

  "tfidf" should {
    "create a distance matrix on sample corpus" in {

      import spire.implicits.DoubleAlgebra
      val vectorizer = TermVectorizer[Double](stopwords)

      implicit val tfidfSpace = TFIDFDocumentVectorSpace[Double](corpus, vectorizer)
      val vectors = corpus.map(vectorizer)

      import axle.jblas.linearAlgebraDoubleMatrix
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      implicit val normedTfidf = tfidfSpace.normed
      val tfidfDistanceMatrix = DistanceMatrix(vectors)

      tfidfDistanceMatrix.distanceMatrix.get(2, 2) must be equalTo 0d
    }
  }

  def tautology[T]: Predicate[T] = new Predicate[T] {
    def apply(a: T) = true
  }

  implicit def eqMapKV[K, V]: Eq[Map[K, V]] = new Eq[Map[K, V]] {
    def eqv(x: Map[K, V], y: Map[K, V]): Boolean = {
      x.equals(y)
    }
  }

  val vectorizer = TermVectorizer[Real](stopwords)

  // TODO combine any 2 from corpus to generate longer sentence, squaring the number of possibilities
  val genTermVector = Gen.oneOf(corpus.map(vectorizer))

  // TODO more possibilities for genReal
  val genReal = Gen.oneOf[Real](1, 2, 3.9, 10)

  val vsl = VectorSpaceLaws[Map[String, Real], Real](
    axle.catsToSpireEq(eqMapKV[String, Real]),
    Arbitrary(genTermVector),
    implicitly[spire.algebra.Eq[Real]],
    Arbitrary(genReal),
    tautology)

  {
    implicit val unweightedSpace = UnweightedDocumentVectorSpace[Real]()

    implicit val normedUnweightedSpace = unweightedSpace.normed

    checkAll("unweighted document vector space (normed)", vsl.normedVectorSpace)
  }

  {
    implicit val tfIdfSpace = TFIDFDocumentVectorSpace[Real](corpus, vectorizer)

    implicit val normedTfIdfSpace = tfIdfSpace.normed

    checkAll("tfidf document vector space (normed)", vsl.normedVectorSpace)
  }

}
