# Clustering

## k-Means Clustering

### Example: Irises

See the wikipedia page on [k-Means Clustering](https://en.wikipedia.org/wiki/K-means_clustering)

A demonstration of k-Means Clustering using the [Iris flower data set](https://en.wikipedia.org/wiki/Iris_flower_data_set)

Imports for Distance quanta

```scala mdoc:silent:reset
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.algebra._
import axle._
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.jung._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

implicit val distanceConverter = {
  import axle.algebra.modules.doubleRationalModule
  Distance.converterGraphK2[Double, DirectedSparseGraph]
}
```

Import the Irises data set

```scala mdoc:silent
import axle.data.Irises
import axle.data.Iris
```

```scala mdoc:silent
val ec = scala.concurrent.ExecutionContext.global
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
implicit val cs = cats.effect.IO.contextShift(ec)

val irisesIO = new Irises[cats.effect.IO](blocker)
val irises = irisesIO.irises.unsafeRunSync()
```

Make a 2-D Euclidean space implicitly available for clustering

```scala mdoc:silent
import org.jblas.DoubleMatrix
import axle.algebra.distance.Euclidean
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.rowVectorInnerProductSpace

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val space: Euclidean[DoubleMatrix, Double] = {
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val inner = rowVectorInnerProductSpace[Int, Int, Double](2)
  new Euclidean[DoubleMatrix, Double]
}
```

Build a classifier of irises based on sepal length and width using the K-Means algorithm

```scala mdoc:silent
import spire.random.Generator.rng
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
import distanceConverter.cm
```

```scala mdoc:silent
val irisFeaturizer =
  (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)

implicit val la = linearAlgebraDoubleMatrix[Double]

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier: KMeans[Iris, List, DoubleMatrix] =
  KMeans[Iris, List, DoubleMatrix](
    irises,
    N = 2,
    irisFeaturizer,
    normalizer,
    K = 3,
    iterations = 20)(rng)
```

Produce a "confusion matrix"

```scala mdoc:silent
import axle.ml.ConfusionMatrix

val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](
  classifier,
  irises.toVector,
  _.species,
  0 to 2)
```

```scala mdoc
confusion.show
```

Visualize the final (two dimensional) centroid positions

```scala mdoc:silent
import axle.visualize.KMeansVisualization
import axle.visualize.Color._

val colors = Vector(red, blue, green)

val vis = KMeansVisualization[Iris, List, DoubleMatrix](classifier, colors)
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

vis.svg[IO]("@DOCWD@/images/k_means.svg").unsafeRunSync()
```

![kmeans](/images/k_means.svg)

Average centroid/cluster vs iteration:

```scala mdoc:silent
import scala.collection.immutable.TreeMap
import axle.visualize._

val plot = Plot(
  () => classifier.distanceLogSeries,
  connect = true,
  drawKey = true,
  colorOf = colors,
  title = Some("KMeans Mean Centroid Distances"),
  xAxis = Some(0d),
  xAxisLabel = Some("step"),
  yAxis = Some(0),
  yAxisLabel = Some("average distance to centroid"))
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/kmeansvsiteration.svg").unsafeRunSync()
```

![kmeans](/images/kmeansvsiteration.svg)

## Example: Federalist Papers

Imports

```scala mdoc:silent:reset
import axle.data.FederalistPapers
import FederalistPapers.Article
```

Download (and cache) the Federalist articles downloader:

```scala mdoc:silent
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

```scala mdoc:silent
import axle.nlp._
import axle.nlp.language.English

import spire.algebra.CRing
implicit val ringLong: CRing[Long] = spire.implicits.LongAlgebra

val corpus = Corpus[Vector, Long](articles.map(_.text).toVector, English)
```

Define a feature extractor using top words and bigrams.

```scala mdoc:height=15
val frequentWords = corpus.wordsMoreFrequentThan(100)
```

```scala mdoc:height=15
val topBigrams = corpus.topKBigrams(200)
```

```scala mdoc
val numDimensions = frequentWords.size + topBigrams.size
```

```scala mdoc:silent
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

```scala mdoc:silent
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

val confusion = ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix](
  classifier,
  articles.toVector,
  _.author,
  0 to 3)
```

```scala mdoc
confusion.show
```
