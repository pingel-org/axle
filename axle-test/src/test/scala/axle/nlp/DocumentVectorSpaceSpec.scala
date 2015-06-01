package axle.nlp

import scala.Vector

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.DistanceMatrix
import axle.jblas.linearAlgebraDoubleMatrix
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.laws.VectorSpaceLaws
import spire.math.Real

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

      1 must be equalTo 1
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

      1 must be equalTo 1
    }
  }

  {
    implicit val predDouble: Predicate[Double] = new Predicate[Double] {
      def apply(a: Double) = true
    }

    def tautology[T]: Predicate[T] = new Predicate[T] {
      def apply(a: T) = true
    }

    implicit def eqMapKV[K: Eq, V: Eq]: Eq[Map[K, V]] = new Eq[Map[K, V]] {
      def eqv(x: Map[K, V], y: Map[K, V]): Boolean = {
        x.equals(y)
      }
    }

    implicit val stringEq = new Eq[String] {
      def eqv(x: String, y: String): Boolean = x equals y
    }

    implicit val eqDouble = new Eq[Double] {
      def eqv(x: Double, y: Double): Boolean = x == y
    }

    implicit val eqInt = new Eq[Int] {
      def eqv(x: Int, y: Int): Boolean = x == y
    }

    val vectorizer = TermVectorizer[Real](stopwords)

    // TODO combine any 2 from corpus to generate longer sentence, squaring the number of possibilities
    // TODO more possibilities for genReal
    val genReal: Gen[Real] = Gen.oneOf[Real](Real(1), Real(2), Real(10))

    val vsl = VectorSpaceLaws[Map[String, Real], Real](
      eqMapKV[String, Real],
      Arbitrary(Gen.oneOf(corpus.map(vectorizer))),
      implicitly[Eq[Real]],
      Arbitrary(genReal),
      tautology)

    implicit val unweightedSpace = UnweightedDocumentVectorSpace[Real]()

    val ipsLaws = vsl.innerProductSpace

    checkAll("unweighted document vector space", ipsLaws)

    implicit val normedUnweightedSpace = unweightedSpace.normed

    val nvsLaws = vsl.normedVectorSpace

    checkAll("unweighted document vector space (normed)", nvsLaws)
  }

}
