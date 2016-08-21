---
layout: page
title: Vector Space Model
permalink: /chapter/vector_space_model/
---

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Vector_space_model">Vector space model</a>

Example
-------

```tut:book
val corpus = Vector(
    "a tall drink of water",
    "the tall dog drinks the water",
    "a quick brown fox jumps the other fox",
    "the lazy dog drinks",
    "the quick brown fox jumps over the lazy dog",
    "the fox and the dog are tall",
    "a fox and a dog are tall",
    "lorem ipsum dolor sit amet"
)
```

### Unweighted Distance

The simplest application of the vector space model to documents is the unweighted space:

```tut:book
import axle.nlp.language.English
import axle.nlp.TermVectorizer
import spire.implicits.DoubleAlgebra

val vectorizer = TermVectorizer[Double](English.stopWords)

val v1 = vectorizer(corpus(1))

val v2 = vectorizer(corpus(2))
```

The object defines a `space` method, which returns a `spire.algebra.MetricSpace` for document vectors:

```tut:book
import axle.nlp.UnweightedDocumentVectorSpace
implicit val unweighted = UnweightedDocumentVectorSpace().normed

unweighted.distance(v1, v2)

unweighted.distance(v1, v1)
```

Compute a "distance matrix" for a given set of vectors using the metric space:

```tut:book
import spire.implicits.DoubleAlgebra
import axle.jblas.linearAlgebraDoubleMatrix
import axle.algebra.DistanceMatrix

val dm = DistanceMatrix(corpus.map(vectorizer))

import axle.string
import axle.jblas.showDoubleMatrix

string(dm.distanceMatrix)

dm.distanceMatrix.max
```

### TF-IDF Distance

```tut:book
import axle.nlp.TFIDFDocumentVectorSpace

val tfidf = TFIDFDocumentVectorSpace(corpus, vectorizer).normed

tfidf.distance(v1, v2)

tfidf.distance(v1, v1)
```
