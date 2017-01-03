---
layout: page
title: Clusters Federalist Papers with k-Means
permalink: /tutorial/cluster_federalist_papers_k_means/
---

Imports

```tut:silent
import axle.data.FederalistPapers._
```

The Federalist articles:

```tut:book
articles.size
```

Construct a `Corpus` object to assist with content analysis

```tut:book
import axle.nlp._
import axle.nlp.language.English

val corpus = Corpus(articles.map(_.text), English)
```

Define a feature extractor using top words and bigrams.

```tut:book
val frequentWords = corpus.wordsMoreFrequentThan(100)

val topBigrams = corpus.topKBigrams(200)

val numDimensions = frequentWords.size + topBigrams.size

def featureExtractor(fp: Article): List[Double] = {
  import axle.enrichGenSeq
  import spire.implicits.LongAlgebra

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

```tut:silent
import spire.implicits._
import spire.algebra._
import axle.ml.distance._
import axle.ml.distance.Euclidean
import org.jblas.DoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

implicit val space = {
  import spire.implicits.IntAlgebra
  import spire.implicits.DoubleAlgebra
  import axle.jblas.moduleDoubleMatrix
  implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](numDimensions)
  Euclidean[DoubleMatrix, Double]
}
```

Create 4 clusters using k-Means

```tut:silent
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
import spire.implicits.DoubleAlgebra
```

```tut:book
val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier = KMeans[Article, List[Article], List[Seq[Double]], DoubleMatrix](
    articles,
    N = numDimensions,
    featureExtractor,
    normalizer,
    K = 4,
    iterations = 100)
```

Show cluster vs author in a confusion matrix:

```tut:silent
import cats.implicits._
import axle.ml.ConfusionMatrix
//import spire.implicits.IntAlgebra
import axle.string
```

```tut:book
val confusion = ConfusionMatrix[Article, Int, String, Vector[Article], DoubleMatrix, Vector[(String, Int)], Vector[String]](
  classifier,
  articles.toVector,
  _.author,
  0 to 3)

string(confusion)
```
