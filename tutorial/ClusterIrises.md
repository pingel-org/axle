---
layout: page
title: Cluster Irises with k-Means Clustering
permalink: /tutorial/cluster_irises_k_means/
---

See the wikipedia page on [k-Means Clustering](https://en.wikipedia.org/wiki/K-means_clustering)

## Clustering Irises

A demonstration of k-Means Clustering using the [Iris flower data set](https://en.wikipedia.org/wiki/Iris_flower_data_set)

Imports for Distance quanta

```scala
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

```scala
import axle.data.Irises
import axle.data.Iris
```

```scala
val ec = scala.concurrent.ExecutionContext.global
// ec: concurrent.ExecutionContextExecutor = scala.concurrent.impl.ExecutionContextImpl$$anon$3@21e5345d[Running, parallelism = 6, size = 4, active = 0, running = 0, steals = 5, tasks = 0, submissions = 0]
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
// blocker: cats.effect.Blocker = cats.effect.Blocker@21e5345d
implicit val cs = cats.effect.IO.contextShift(ec)
// cs: cats.effect.ContextShift[cats.effect.IO] = cats.effect.internals.IOContextShift@2de9c04f

val irisesIO = new Irises[cats.effect.IO](blocker)
// irisesIO: Irises[cats.effect.IO] = axle.data.Irises@a66ece4
val irises = irisesIO.irises.unsafeRunSync()
// irises: List[Iris] = List(
//   Iris(
//     sepalLength = UnittedQuantity(
//       magnitude = 5.1,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
//         symbol = "cm",
//         wikipediaUrl = None
//       )
//     ),
//     sepalWidth = UnittedQuantity(
//       magnitude = 3.5,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
//         symbol = "cm",
//         wikipediaUrl = None
//       )
//     ),
//     petalLength = UnittedQuantity(
//       magnitude = 1.4,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
//         symbol = "cm",
//         wikipediaUrl = None
//       )
//     ),
//     petalWidth = UnittedQuantity(
//       magnitude = 0.2,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
//         symbol = "cm",
//         wikipediaUrl = None
//       )
//     ),
//     species = "Iris-setosa"
//   ),
//   Iris(
//     sepalLength = UnittedQuantity(
//       magnitude = 4.9,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
//         symbol = "cm",
//         wikipediaUrl = None
//       )
//     ),
//     sepalWidth = UnittedQuantity(
//       magnitude = 3.0,
//       unit = UnitOfMeasurement(
//         name = "centimeter",
// ...
```

Make a 2-D Euclidean space implicitly available for clustering

```scala
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

```scala
import spire.random.Generator.rng
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
import distanceConverter.cm
```

```scala
val irisFeaturizer =
  (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)
// irisFeaturizer: Iris => List[Double] = <function1>

implicit val la = linearAlgebraDoubleMatrix[Double]
// la: algebra.LinearAlgebra[DoubleMatrix, Int, Int, Double] = axle.jblas.package$$anon$5@545bab8d

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)
// normalizer: DoubleMatrix => PCAFeatureNormalizer[DoubleMatrix] = scala.Function2$$Lambda$9972/0x0000000802634000@388a0aed

val classifier: KMeans[Iris, List, DoubleMatrix] =
  KMeans[Iris, List, DoubleMatrix](
    irises,
    N = 2,
    irisFeaturizer,
    normalizer,
    K = 3,
    iterations = 20)(rng)
// classifier: KMeans[Iris, List, DoubleMatrix] = KMeans(
//   data = List(
//     Iris(
//       sepalLength = UnittedQuantity(
//         magnitude = 5.1,
//         unit = UnitOfMeasurement(
//           name = "centimeter",
//           symbol = "cm",
//           wikipediaUrl = None
//         )
//       ),
//       sepalWidth = UnittedQuantity(
//         magnitude = 3.5,
//         unit = UnitOfMeasurement(
//           name = "centimeter",
//           symbol = "cm",
//           wikipediaUrl = None
//         )
//       ),
//       petalLength = UnittedQuantity(
//         magnitude = 1.4,
//         unit = UnitOfMeasurement(
//           name = "centimeter",
//           symbol = "cm",
//           wikipediaUrl = None
//         )
//       ),
//       petalWidth = UnittedQuantity(
//         magnitude = 0.2,
//         unit = UnitOfMeasurement(
//           name = "centimeter",
//           symbol = "cm",
//           wikipediaUrl = None
//         )
//       ),
//       species = "Iris-setosa"
//     ),
//     Iris(
//       sepalLength = UnittedQuantity(
//         magnitude = 4.9,
//         unit = UnitOfMeasurement(
//           name = "centimeter",
//           symbol = "cm",
//           wikipediaUrl = None
//         )
//       ),
//       sepalWidth = UnittedQuantity(
//         magnitude = 3.0,
//         unit = UnitOfMeasurement(
// ...
```

Produce a "confusion matrix"

```scala
import axle.ml.ConfusionMatrix
```

```scala
val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](
  classifier,
  irises.toVector,
  _.species,
  0 to 2)
// confusion: ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix] = ConfusionMatrix(
//   classifier = KMeans(
//     data = List(
//       Iris(
//         sepalLength = UnittedQuantity(
//           magnitude = 5.1,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         sepalWidth = UnittedQuantity(
//           magnitude = 3.5,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         petalLength = UnittedQuantity(
//           magnitude = 1.4,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         petalWidth = UnittedQuantity(
//           magnitude = 0.2,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         species = "Iris-setosa"
//       ),
//       Iris(
//         sepalLength = UnittedQuantity(
//           magnitude = 4.9,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         sepalWidth = UnittedQuantity(
//           magnitude = 3.0,
// ...

confusion.show
// res0: String = """  1  49   0 :  50 Iris-setosa
//  34   0  16 :  50 Iris-versicolor
//  16   0  34 :  50 Iris-virginica
// 
//  51  49  50
// """
```

Visualize the final (two dimensional) centroid positions

```scala
import axle.visualize.KMeansVisualization
import axle.visualize.Color._
```

```scala
val colors = Vector(red, blue, green)
// colors: Vector[visualize.Color] = Vector(
//   Color(r = 255, g = 0, b = 0),
//   Color(r = 0, g = 0, b = 255),
//   Color(r = 0, g = 255, b = 0)
// )

val vis = KMeansVisualization[Iris, List, DoubleMatrix](classifier, colors)
// vis: KMeansVisualization[Iris, List, DoubleMatrix] = KMeansVisualization(
//   classifier = KMeans(
//     data = List(
//       Iris(
//         sepalLength = UnittedQuantity(
//           magnitude = 5.1,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         sepalWidth = UnittedQuantity(
//           magnitude = 3.5,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         petalLength = UnittedQuantity(
//           magnitude = 1.4,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         petalWidth = UnittedQuantity(
//           magnitude = 0.2,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         species = "Iris-setosa"
//       ),
//       Iris(
//         sepalLength = UnittedQuantity(
//           magnitude = 4.9,
//           unit = UnitOfMeasurement(
//             name = "centimeter",
//             symbol = "cm",
//             wikipediaUrl = None
//           )
//         ),
//         sepalWidth = UnittedQuantity(
//           magnitude = 3.0,
// ...

import axle.web._
import cats.effect._

vis.svg[IO]("k_means.svg").unsafeRunSync()
```

![kmeans](/tutorial/images/k_means.svg)

Average centroid/cluster vs iteration:

```scala
import scala.collection.immutable.TreeMap
import axle.visualize._
```

```scala
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
// plot: Plot[Int, Int, Double, TreeMap[Int, Double]] = Plot(
//   dataFn = <function0>,
//   connect = true,
//   drawKey = true,
//   width = 700,
//   height = 600,
//   border = 50,
//   pointDiameter = 4,
//   keyLeftPadding = 20,
//   keyTopPadding = 50,
//   keyWidth = 80,
//   fontName = "Courier New",
//   fontSize = 12,
//   bold = false,
//   titleFontName = "Palatino",
//   titleFontSize = 20,
//   colorOf = Vector(
//     Color(r = 255, g = 0, b = 0),
//     Color(r = 0, g = 0, b = 255),
//     Color(r = 0, g = 255, b = 0)
//   ),
//   title = Some(value = "KMeans Mean Centroid Distances"),
//   keyTitle = None,
//   xAxis = Some(value = 0.0),
//   xAxisLabel = Some(value = "step"),
//   yAxis = Some(value = 0),
//   yAxisLabel = Some(value = "average distance to centroid")
// )

import axle.web._
import cats.effect._

plot.svg[IO]("kmeansvsiteration.svg").unsafeRunSync()
```

![kmeans](/tutorial/images/kmeansvsiteration.svg)
