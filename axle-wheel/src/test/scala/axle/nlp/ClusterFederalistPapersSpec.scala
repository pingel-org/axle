package axle.nlp

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext

import cats.effect._
import cats.implicits._

import spire.algebra._
import spire.random.Generator.rng

import axle.nlp.language.English

class ClusterFederalistPapersSpec extends AnyFunSuite with Matchers {

  val ec = ExecutionContext.global
  val blocker = Blocker.liftExecutionContext(ec)
  implicit val cs = IO.contextShift(ec)

  test("k-means clusters federal papers") {

    import axle.data.FederalistPapers._

    val artcls = articles[IO](blocker).unsafeRunSync()

    val corpus = Corpus(artcls.map(_.text), English)

    val frequentWords = corpus.wordsMoreFrequentThan(100)
    val topBigrams = corpus.topKBigrams(200)
    val numDimensions = frequentWords.size + topBigrams.size

    def featureExtractor(fp: Article): List[Double] = {

      import axle.enrichGenSeq
      implicit val ringLong: Ring[Long] = spire.implicits.LongAlgebra

      val tokens = English.tokenize(fp.text.toLowerCase)
      val wordCounts = tokens.tally[Long]
      val bigramCounts = bigrams(tokens).tally[Long]
      val wordFeatures = frequentWords.map(wordCounts(_) + 0.1)
      val bigramFeatures = topBigrams.map(bigramCounts(_) + 0.1)
      wordFeatures ++ bigramFeatures
    }

    import org.jblas.DoubleMatrix
    import axle.algebra.distance._
    import axle.algebra.distance.Euclidean
    import axle.jblas.linearAlgebraDoubleMatrix

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

    implicit val space = {
      implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
      implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](numDimensions)
      new Euclidean[DoubleMatrix, Double]
    }

    import axle.ml.KMeans
    import axle.ml.PCAFeatureNormalizer

    val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

    val classifier = KMeans[Article, List, DoubleMatrix](
      artcls,
      N = numDimensions,
      featureExtractor,
      normalizer,
      K = 4,
      iterations = 100)(rng)

    import axle.ml.ConfusionMatrix

    val confusion = ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix](
      classifier,
      artcls.toVector,
      _.author,
      0 to 3)

    corpus.show should include("Top 10 words")
    artcls.size should be > 50
    confusion.counts.rows should be(5)
  }

}
