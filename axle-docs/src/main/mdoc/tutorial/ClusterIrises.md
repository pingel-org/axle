---
layout: page
title: Cluster Irises with k-Means Clustering
permalink: /tutorial/cluster_irises_k_means/
---

See the wikipedia page on [k-Means Clustering](https://en.wikipedia.org/wiki/K-means_clustering)

## Clustering Irises

A demonstration of k-Means Clustering using the [Iris flower data set](https://en.wikipedia.org/wiki/Iris_flower_data_set)

Imports for Distance quanta

```scala mdoc:silent
import cats.implicits._
import axle._
import axle.quanta.Distance
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement

implicit val distanceConverter = {
  import spire.implicits.DoubleAlgebra
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
val irisesData = new Irises
```

Make a 2-D Euclidean space implicitly available for clustering

```scala mdoc:silent
import axle.ml.distance.Euclidean
import org.jblas.DoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

implicit val space = {
  import spire.implicits.IntAlgebra
  import spire.implicits.DoubleAlgebra
  implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](2)
  Euclidean[DoubleMatrix, Double]
}
```

Build a classifier of irises based on sepal length and width using the K-Means algorithm

```scala mdoc:silent
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
import distanceConverter.cm
import spire.implicits.DoubleAlgebra
```

```scala mdoc
val irisFeaturizer = (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier = KMeans[Iris, List[Iris], List[Seq[Double]], DoubleMatrix](
    irisesData.irises,
    N = 2,
    irisFeaturizer,
    normalizer,
    K = 3,
    iterations = 20)
```

Produce a "confusion matrix"

```scala mdoc:silent
import axle.ml.ConfusionMatrix
import spire.implicits.IntAlgebra
```

```scala mdoc
val confusion = ConfusionMatrix[Iris, Int, String, Vector[Iris], DoubleMatrix, Vector[(String, Int)], Vector[String]](
  classifier,
  irisesData.irises.toVector,
  _.species,
  0 to 2)

string(confusion)
```

Visualize the final (two dimensional) centroid positions

```scala mdoc:silent
import axle.web._
import axle.visualize.KMeansVisualization
import axle.visualize.Color._
```

```scala mdoc
val colors = Vector(red, blue, green)

val vis = KMeansVisualization(classifier, colors)

svg(vis, "kmeans.svg")
```

![kmeans](/tutorial/images/kmeans.svg)

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
svg(plot, "kmeansvsiteration.svg")
```

![kmeans](/tutorial/images/kmeansvsiteration.svg)
