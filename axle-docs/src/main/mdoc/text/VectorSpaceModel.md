# Vector Space Model

See the Wikipedia page on [Vector space model](https://en.wikipedia.org/wiki/Vector_space_model)

## Example

```scala mdoc:silent
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

```scala mdoc:silent
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.nlp.language.English
import axle.nlp.TermVectorizer

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

val vectorizer = TermVectorizer[Double](English.stopWords)
```

```scala mdoc
val v1 = vectorizer(corpus(1))

val v2 = vectorizer(corpus(2))
```

The object defines a `space` method, which returns a `spire.algebra.MetricSpace` for document vectors:

```scala mdoc:silent
import axle.nlp.UnweightedDocumentVectorSpace

implicit val unweighted = UnweightedDocumentVectorSpace().normed
```

```scala mdoc
unweighted.distance(v1, v2)

unweighted.distance(v1, v1)
```

Compute a "distance matrix" for a given set of vectors using the metric space:

```scala mdoc:silent
import axle.jblas._
import axle.algebra.DistanceMatrix

val dm = DistanceMatrix(corpus.map(vectorizer))
```

```scala mdoc
dm.distanceMatrix.show

dm.distanceMatrix.max
```

### TF-IDF Distance

```scala mdoc:silent
import axle.nlp.TFIDFDocumentVectorSpace

val tfidf = TFIDFDocumentVectorSpace(corpus, vectorizer).normed
```

```scala mdoc
tfidf.distance(v1, v2)

tfidf.distance(v1, v1)
```
