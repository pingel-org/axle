# Cluster Irises

## With k-Means Clustering

See the wikipedia page on [k-Means Clustering](https://en.wikipedia.org/wiki/K-means_clustering)

A demonstration of k-Means Clustering using the [Iris flower data set](https://en.wikipedia.org/wiki/Iris_flower_data_set)

Imports for Distance quanta

```scala mdoc:silent
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

```scala mdoc
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

```scala mdoc
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
```

```scala mdoc
val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](
  classifier,
  irises.toVector,
  _.species,
  0 to 2)

confusion.show
```

Visualize the final (two dimensional) centroid positions

```scala mdoc:silent
import axle.visualize.KMeansVisualization
import axle.visualize.Color._
```

```scala mdoc
val colors = Vector(red, blue, green)

val vis = KMeansVisualization[Iris, List, DoubleMatrix](classifier, colors)

import axle.web._
import cats.effect._

vis.svg[IO]("@DOCWD@/images/k_means.svg").unsafeRunSync()
```

![kmeans](/images/k_means.svg)

Average centroid/cluster vs iteration:

```scala mdoc:silent
import scala.collection.immutable.TreeMap
import axle.visualize._
```

```scala mdoc
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

import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/kmeansvsiteration.svg").unsafeRunSync()
```

![kmeans](/images/kmeansvsiteration.svg)
