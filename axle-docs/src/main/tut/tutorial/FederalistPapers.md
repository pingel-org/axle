---
layout: page
title: Federalist Papers
permalink: /tutorial/federalist_papers/
---

Imports

```tut:silent
import axle.data.FederalistPapers._
import axle.nlp._
import axle.ml._
import axle.ml.distance._
import axle.matrix._
import spire.implicits._
import spire.algebra._
import KMeansModule._
import JblasMatrixModule._
import axle.nlp.language.English
import scala.collection.GenSeq
``

The Federalist articles:

```tut:book
articles.size
```

Construct a `Corpus`

```tut:book
val corpus = new Corpus(articles.map(_.text), English)
```

Now attempt to cluster them using as features: top 100 words and top 200 bigrams.

```tut:book
val topWords = corpus.topWords(100)

val topBigrams = corpus.topBigrams(200)

val numDimensions = topWords.size + topBigrams.size

implicit val space: MetricSpace[Matrix[Double], Double] = Euclidian(numDimensions)

def featureExtractor(fp: Article) = {
  val tokens = English.tokenize(fp.text.toLowerCase)
  val wordCounts = tokens.tally
  val bigramCounts =  bigrams(tokens).tally
  val wordFeatures = topWords.map(wordCounts(_) + 0.1)
  val bigramFeatures = topBigrams.map(bigramCounts(_) + 0.1)
  wordFeatures ++ bigramFeatures
}
```

Create 4 clusters using k-Means

```tut:book
val f = classifier(
  articles,
  N = numDimensions,
  featureExtractor,
  (xs: Seq[Double]) => Article(0, "", "", ""),
  K = 4,
  iterations = 100)
```

Show cluster vs author in a confusion matrix:

```tut:book
f.confusionMatrix(articles, _.author)
```
