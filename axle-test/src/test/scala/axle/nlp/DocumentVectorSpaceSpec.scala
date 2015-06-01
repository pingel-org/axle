package axle.nlp

import scala.Vector

import org.specs2.mutable.Specification

import axle.algebra.DistanceMatrix
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline
import spire.algebra.Eq
import spire.algebra.InnerProductSpace
import spire.laws.VectorSpaceLaws

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

  implicit val pred: Predicate[Double] = new Predicate[Double] {
    def apply(a: Double) = true
  }

  implicit def eqMapKV[K: Eq, V: Eq]: Eq[Map[K, V]] = new Eq[Map[K, V]] {
    def eqv(x: Map[K, V], y: Map[K, V]): Boolean = {
      x.equals(y)
    }
  }

  implicit val stringEq = new Eq[String] {
    def eqv(x: String, y: String): Boolean = x equals y
  }

  import spire.implicits.DoubleAlgebra

  {
    /*
    val vectorizer = TermVectorizer[Double](stopwords)

    implicit val unweightedSpace = UnweightedDocumentVectorSpace[Double]()

    import org.scalacheck.Gen
    import org.scalacheck.Gen.Choose

    implicit val genWord = Gen.oneOf[String](
      "the", "quick", "brown", "fox", "jumped", "over", "lazy", "dog")

    implicit val genDouble = Gen.choose[Double](1d, 10d)

    checkAll("unweighted document vector space",
      VectorSpaceLaws[Map[String, Double], Double].innerProductSpace)

    implicit val normedUnweightedSpace = unweightedSpace.normed

    checkAll("unweighted document vector space (normed)",
      VectorSpaceLaws[Map[String, Double], Double].normedVectorSpace)
    */
  }

}
