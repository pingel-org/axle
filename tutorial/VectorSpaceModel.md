---
layout: page
title: Vector Space Model
permalink: /tutorial/vector_space_model/
---

See the Wikipedia page on [Vector space model](https://en.wikipedia.org/wiki/Vector_space_model)

## Example

```scala
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
// corpus: Vector[String] = Vector(
//   "a tall drink of water",
//   "the tall dog drinks the water",
//   "a quick brown fox jumps the other fox",
//   "the lazy dog drinks",
//   "the quick brown fox jumps over the lazy dog",
//   "the fox and the dog are tall",
//   "a fox and a dog are tall",
//   "lorem ipsum dolor sit amet"
// )
```

### Unweighted Distance

The simplest application of the vector space model to documents is the unweighted space:

```scala
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.nlp.language.English
import axle.nlp.TermVectorizer

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
// fieldDouble: Field[Double] = spire.std.DoubleAlgebra@38677cfc
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
// nrootDouble: NRoot[Double] = spire.std.DoubleAlgebra@38677cfc

val vectorizer = TermVectorizer[Double](English.stopWords)
// vectorizer: TermVectorizer[Double] = TermVectorizer(
//   stopwords = HashSet(
//     "for",
//     "but",
//     "if",
//     "it",
//     "a",
//     "as",
//     "or",
//     "that",
//     "to",
//     "on",
//     "an",
//     "no",
//     "the",
//     "this",
//     "in",
//     "are",
//     "is",
//     "such",
//     "they",
//     "these",
//     "was",
//     "there",
//     "at",
//     "by",
//     "then",
//     "will",
//     "their",
//     "not",
//     "with",
//     "be",
//     "into",
//     "of",
//     "and"
//   )
// )

val v1 = vectorizer(corpus(1))
// v1: Map[String, Double] = Map(
//   "tall" -> 1.0,
//   "dog" -> 1.0,
//   "drinks" -> 1.0,
//   "water" -> 1.0
// )

val v2 = vectorizer(corpus(2))
// v2: Map[String, Double] = Map(
//   "brown" -> 1.0,
//   "quick" -> 1.0,
//   "jumps" -> 1.0,
//   "fox" -> 2.0,
//   "other" -> 1.0
// )
```

The object defines a `space` method, which returns a `spire.algebra.MetricSpace` for document vectors:

```scala
import axle.nlp.UnweightedDocumentVectorSpace
implicit val unweighted = UnweightedDocumentVectorSpace().normed
// unweighted: spire.algebra.NormedVectorSpace[Map[String, Double], Double] = spire.algebra.InnerProductSpace$$anon$1@79fded81

unweighted.distance(v1, v2)
// res0: Double = 3.4641016151377544

unweighted.distance(v1, v1)
// res1: Double = 0.0
```

Compute a "distance matrix" for a given set of vectors using the metric space:

```scala
import axle.jblas._
import axle.algebra.DistanceMatrix

val dm = DistanceMatrix(corpus.map(vectorizer))
// dm: DistanceMatrix[Map[String, Double], Vector, org.jblas.DoubleMatrix] = DistanceMatrix(
//   vectors = Vector(
//     Map("tall" -> 1.0, "drink" -> 1.0, "water" -> 1.0),
//     Map("tall" -> 1.0, "dog" -> 1.0, "drinks" -> 1.0, "water" -> 1.0),
//     Map(
//       "brown" -> 1.0,
//       "quick" -> 1.0,
//       "jumps" -> 1.0,
//       "fox" -> 2.0,
//       "other" -> 1.0
//     ),
//     Map("lazy" -> 1.0, "dog" -> 1.0, "drinks" -> 1.0),
//     Map(
//       "lazy" -> 1.0,
//       "dog" -> 1.0,
//       "over" -> 1.0,
//       "brown" -> 1.0,
//       "quick" -> 1.0,
//       "jumps" -> 1.0,
//       "fox" -> 1.0
//     ),
//     Map("fox" -> 1.0, "dog" -> 1.0, "tall" -> 1.0),
//     Map("fox" -> 1.0, "dog" -> 1.0, "tall" -> 1.0),
//     Map(
//       "sit" -> 1.0,
//       "ipsum" -> 1.0,
//       "lorem" -> 1.0,
//       "amet" -> 1.0,
//       "dolor" -> 1.0
//     )
//   )
// )

dm.distanceMatrix.show
// res2: String = """0.000000 1.732051 3.316625 2.449490 3.162278 2.000000 2.000000 2.828427
// 1.732051 0.000000 3.464102 1.732051 3.000000 1.732051 1.732051 3.000000
// 3.316625 3.464102 0.000000 3.316625 2.236068 2.645751 2.645751 3.605551
// 2.449490 1.732051 3.316625 0.000000 2.449490 2.000000 2.000000 2.828427
// 3.162278 3.000000 2.236068 2.449490 0.000000 2.449490 2.449490 3.464102
// 2.000000 1.732051 2.645751 2.000000 2.449490 0.000000 0.000000 2.828427
// 2.000000 1.732051 2.645751 2.000000 2.449490 0.000000 0.000000 2.828427
// 2.828427 3.000000 3.605551 2.828427 3.464102 2.828427 2.828427 0.000000"""

dm.distanceMatrix.max
// res3: Double = 3.605551275463989
```

### TF-IDF Distance

```scala
import axle.nlp.TFIDFDocumentVectorSpace

val tfidf = TFIDFDocumentVectorSpace(corpus, vectorizer).normed
// tfidf: spire.algebra.NormedVectorSpace[Map[String, Double], Double] = spire.algebra.InnerProductSpace$$anon$1@471da73d

tfidf.distance(v1, v2)
// res4: Double = 4.068944074907273

tfidf.distance(v1, v1)
// res5: Double = 0.0
```
