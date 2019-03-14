package axle.nlp

import scala.Vector

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest._
import org.typelevel.discipline.scalatest.Discipline
import org.typelevel.discipline.Predicate

import cats.implicits._
import spire.algebra._
import spire.laws.VectorSpaceLaws
import spire.math.Real
import axle.algebra.DistanceMatrix

class DocumentVectorSpaceSpec
  extends FunSuite with Matchers
  with Discipline {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("TermVectorizer creates term vectors correctly") {

    val stopwords = Set("this", "the")
    val vectorizer = TermVectorizer[Double](stopwords)

    val lines = Vector("foo bar baz", "foo fu", "fu fu fu bar")

    vectorizer.wordCount(lines) should be(Map("foo" -> 2d, "bar" -> 2d, "baz" -> 1d, "fu" -> 4d))
    vectorizer.wordExistsCount(lines) should be(Map("foo" -> 2d, "bar" -> 2d, "baz" -> 1d, "fu" -> 2d))
  }

  val stopwords = Set("the", "a", "of", "for", "in").toSet

  val corpus = Vector(
    "the quick brown fox jumps over the lazy dog",
    "fox jumps over dog eden was the sumerian word edine",
    "hostname and fqdn for the instance can be critical if you're automating deployment with",
    "quick lazy word",
    "foo bar dog")

  test("DocumentVectorSpace creates a distance matrix on sample corpus") {

    val vectorizer = TermVectorizer[Double](stopwords)

    val unweightedSpace = UnweightedDocumentVectorSpace[Double]()

    import axle.jblas.linearAlgebraDoubleMatrix
    implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
    implicit val normedUnweightedSpace = unweightedSpace.normed

    val unweightedDistanceMatrix = DistanceMatrix(corpus.map(vectorizer))

    unweightedDistanceMatrix.distanceMatrix.get(2, 2) should be(0d)
  }

  test("tfidf creates a distance matrix on sample corpus") {

    val vectorizer = TermVectorizer[Double](stopwords)

    implicit val tfidfSpace = TFIDFDocumentVectorSpace[Double](corpus, vectorizer)
    val vectors = corpus.map(vectorizer)

    import axle.jblas.linearAlgebraDoubleMatrix
    implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

    implicit val normedTfidf = tfidfSpace.normed
    val tfidfDistanceMatrix = DistanceMatrix(vectors)

    tfidfDistanceMatrix.distanceMatrix.get(2, 2) should be(0d)
  }

  implicit def eqMapKV[K, V] = Eq.fromUniversalEquals[Map[K, V]]

  val vectorizer = TermVectorizer[Real](stopwords)

  // TODO combine any 2 from corpus to generate longer sentence, squaring the number of possibilities
  val genTermVector = Gen.oneOf(corpus.map(vectorizer))

  val spireEqMapStringReal = eqMapKV[String, Real]

  val vsl = VectorSpaceLaws[Map[String, Real], Real](
    spireEqMapStringReal,
    Arbitrary(genTermVector),
    implicitly[spire.algebra.Eq[Real]],
    Arbitrary(spire.laws.gen.real.filter(x => x.doubleValue != 0d)),
    Predicate.const[Real](true))

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
