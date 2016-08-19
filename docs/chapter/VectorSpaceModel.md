Vector Space Model
==================

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Vector_space_model">Vector space model</a>

Example
-------

```scala
scala> val corpus = Vector(
     |     "a tall drink of water",
     |     "the tall dog drinks the water",
     |     "a quick brown fox jumps the other fox",
     |     "the lazy dog drinks",
     |     "the quick brown fox jumps over the lazy dog",
     |     "the fox and the dog are tall",
     |     "a fox and a dog are tall",
     |     "lorem ipsum dolor sit amet"
     | )
corpus: scala.collection.immutable.Vector[String] = Vector(a tall drink of water, the tall dog drinks the water, a quick brown fox jumps the other fox, the lazy dog drinks, the quick brown fox jumps over the lazy dog, the fox and the dog are tall, a fox and a dog are tall, lorem ipsum dolor sit amet)
```

### Unweighted Distance

The simplest application of the vector space model to documents is the unweighted space:

```scala
scala> import axle.nlp.language.English
import axle.nlp.language.English

scala> import axle.nlp.TermVectorizer
import axle.nlp.TermVectorizer

scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> val vectorizer = TermVectorizer[Double](English.stopWords)
vectorizer: axle.nlp.TermVectorizer[Double] = <function1>

scala> val v1 = vectorizer(corpus(1))
v1: Map[String,Double] = Map(tall -> 1.0, dog -> 1.0, drinks -> 1.0, water -> 1.0)

scala> val v2 = vectorizer(corpus(2))
v2: Map[String,Double] = Map(brown -> 1.0, quick -> 1.0, jumps -> 1.0, fox -> 2.0, other -> 1.0)
```

The object defines a `space` method, which returns a `spire.algebra.MetricSpace` for document vectors:

```scala
scala> import axle.nlp.UnweightedDocumentVectorSpace
import axle.nlp.UnweightedDocumentVectorSpace

scala> implicit val unweighted = UnweightedDocumentVectorSpace().normed
unweighted: spire.algebra.NormedVectorSpace[Map[String,Double],Double] = spire.algebra.InnerProductSpace$$anon$3@eeaadea

scala> unweighted.distance(v1, v2)
res0: Double = 3.4641016151377544

scala> unweighted.distance(v1, v1)
res1: Double = 0.0
```

Compute a "distance matrix" for a given set of vectors using the metric space:

```scala
scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

scala> import axle.algebra.DistanceMatrix
import axle.algebra.DistanceMatrix

scala> val dm = DistanceMatrix(corpus.map(vectorizer))
dm: axle.algebra.DistanceMatrix[Map[String,Double],scala.collection.immutable.Vector[Map[String,Double]],org.jblas.DoubleMatrix] = DistanceMatrix(Vector(Map(tall -> 1.0, drink -> 1.0, water -> 1.0), Map(tall -> 1.0, dog -> 1.0, drinks -> 1.0, water -> 1.0), Map(brown -> 1.0, quick -> 1.0, jumps -> 1.0, fox -> 2.0, other -> 1.0), Map(lazy -> 1.0, dog -> 1.0, drinks -> 1.0), Map(lazy -> 1.0, dog -> 1.0, over -> 1.0, brown -> 1.0, quick -> 1.0, jumps -> 1.0, fox -> 1.0), Map(fox -> 1.0, dog -> 1.0, tall -> 1.0), Map(fox -> 1.0, dog -> 1.0, tall -> 1.0), Map(sit -> 1.0, ipsum -> 1.0, lorem -> 1.0, amet -> 1.0, dolor -> 1.0)))

scala> import axle.string
import axle.string

scala> import axle.jblas.showDoubleMatrix
import axle.jblas.showDoubleMatrix

scala> string(dm.distanceMatrix)
res2: String =
0.000000 1.732051 3.316625 2.449490 3.162278 2.000000 2.000000 2.828427
1.732051 0.000000 3.464102 1.732051 3.000000 1.732051 1.732051 3.000000
3.316625 3.464102 0.000000 3.316625 2.236068 2.645751 2.645751 3.605551
2.449490 1.732051 3.316625 0.000000 2.449490 2.000000 2.000000 2.828427
3.162278 3.000000 2.236068 2.449490 0.000000 2.449490 2.449490 3.464102
2.000000 1.732051 2.645751 2.000000 2.449490 0.000000 0.000000 2.828427
2.000000 1.732051 2.645751 2.000000 2.449490 0.000000 0.000000 2.828427
2.828427 3.000000 3.605551 2.828427 3.464102 2.828427 2.828427 0.000000

scala> dm.distanceMatrix.max
res3: Double = 3.605551275463989
```

### TF-IDF Distance

```scala
scala> import axle.nlp.TFIDFDocumentVectorSpace
import axle.nlp.TFIDFDocumentVectorSpace

scala> val tfidf = TFIDFDocumentVectorSpace(corpus, vectorizer).normed
tfidf: spire.algebra.NormedVectorSpace[Map[String,Double],Double] = spire.algebra.InnerProductSpace$$anon$3@525a66e

scala> tfidf.distance(v1, v2)
res4: Double = 4.068944074907273

scala> tfidf.distance(v1, v1)
res5: Double = 0.0
```
