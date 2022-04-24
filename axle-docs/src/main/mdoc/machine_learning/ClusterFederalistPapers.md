# Clusters Federalist Papers

## With k-Means

Imports

```scala mdoc:silent
import axle.data.FederalistPapers
import FederalistPapers.Article
```

Download (and cache) the Federalist articles downloader:

```scala mdoc
val ec = scala.concurrent.ExecutionContext.global
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
implicit val cs = cats.effect.IO.contextShift(ec)

val articlesIO = FederalistPapers.articles[cats.effect.IO](blocker)

val articles = articlesIO.unsafeRunSync()
```

The result is a `List[Article]`.  How many articles are there?

```scala mdoc
articles.size
```

Construct a `Corpus` object to assist with content analysis

```scala mdoc
import axle.nlp._
import axle.nlp.language.English

import spire.algebra.CRing
implicit val ringLong: CRing[Long] = spire.implicits.LongAlgebra

val corpus = Corpus[Vector, Long](articles.map(_.text).toVector, English)
```

Define a feature extractor using top words and bigrams.

```scala mdoc
val frequentWords = corpus.wordsMoreFrequentThan(100)

val topBigrams = corpus.topKBigrams(200)

val numDimensions = frequentWords.size + topBigrams.size

import axle.syntax.talliable.talliableOps

def featureExtractor(fp: Article): List[Double] = {

  val tokens = English.tokenize(fp.text.toLowerCase)
  val wordCounts = tokens.tally[Long]
  val bigramCounts =  bigrams(tokens).tally[Long]
  val wordFeatures = frequentWords.map(wordCounts(_) + 0.1)
  val bigramFeatures = topBigrams.map(bigramCounts(_) + 0.1)
  wordFeatures ++ bigramFeatures
}
```

Place a `MetricSpace` implicitly in scope that defines the space in which to
measure similarity of Articles.

```scala mdoc:silent
import spire.algebra._

import axle.algebra.distance.Euclidean

import org.jblas.DoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val space = {
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](numDimensions)
  new Euclidean[DoubleMatrix, Double]
}
```

Create 4 clusters using k-Means

```scala mdoc:silent
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
```

```scala mdoc
import cats.implicits._
import spire.random.Generator.rng

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier = KMeans[Article, List, DoubleMatrix](
  articles,
  N = numDimensions,
  featureExtractor,
  normalizer,
  K = 4,
  iterations = 100)(rng)
```

Show cluster vs author in a confusion matrix:

```scala mdoc:silent
import axle.ml.ConfusionMatrix
```

```scala mdoc
val confusion = ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix](
  classifier,
  articles.toVector,
  _.author,
  0 to 3)

confusion.show
```
