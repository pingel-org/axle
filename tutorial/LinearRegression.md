---
layout: page
title: Linear Regression
permalink: /tutorial/linear_regression/
---

`axle.ml.LinearRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Linear Regression](https://en.wikipedia.org/wiki/Linear_regression)

## Predicting Home Prices

```scala
case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

val listings = List(
  RealtyListing(2104, 5, 1, 45, 460d),
  RealtyListing(1416, 3, 2, 40, 232d),
  RealtyListing(1534, 3, 2, 30, 315d),
  RealtyListing(852, 2, 1, 36, 178d))
// listings: List[RealtyListing] = List(
//   RealtyListing(
//     size = 2104.0,
//     bedrooms = 5,
//     floors = 1,
//     age = 45,
//     price = 460.0
//   ),
//   RealtyListing(
//     size = 1416.0,
//     bedrooms = 3,
//     floors = 2,
//     age = 40,
//     price = 232.0
//   ),
//   RealtyListing(
//     size = 1534.0,
//     bedrooms = 3,
//     floors = 2,
//     age = 30,
//     price = 315.0
//   ),
//   RealtyListing(size = 852.0, bedrooms = 2, floors = 1, age = 36, price = 178.0)
// )
```

Create a price estimator using linear regression.

```scala
import cats.implicits._
import spire.algebra.Rng
import spire.algebra.NRoot
import axle.jblas._

implicit val rngDouble: Rng[Double] = spire.implicits.DoubleAlgebra
// rngDouble: Rng[Double] = spire.std.DoubleAlgebra@454746e2
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
// nrootDouble: NRoot[Double] = spire.std.DoubleAlgebra@454746e2
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
// laJblasDouble: axle.algebra.LinearAlgebra[org.jblas.DoubleMatrix, Int, Int, Double] = axle.jblas.package$$anon$5@39e42070
implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra
// rngInt: Rng[Int] = spire.std.IntAlgebra@4438df1b

import axle.ml.LinearRegression

val priceEstimator = LinearRegression(
  listings,
  numFeatures = 4,
  featureExtractor = (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
  objectiveExtractor = (rl: RealtyListing) => rl.price,
  α = 0.1,
  iterations = 100)
// priceEstimator: LinearRegression[RealtyListing, org.jblas.DoubleMatrix] = LinearRegression(
//   examples = List(
//     RealtyListing(
//       size = 2104.0,
//       bedrooms = 5,
//       floors = 1,
//       age = 45,
//       price = 460.0
//     ),
//     RealtyListing(
//       size = 1416.0,
//       bedrooms = 3,
//       floors = 2,
//       age = 40,
//       price = 232.0
//     ),
//     RealtyListing(
//       size = 1534.0,
//       bedrooms = 3,
//       floors = 2,
//       age = 30,
//       price = 315.0
//     ),
//     RealtyListing(
//       size = 852.0,
//       bedrooms = 2,
//       floors = 1,
//       age = 36,
//       price = 178.0
//     )
//   ),
//   numFeatures = 4,
//   featureExtractor = <function1>,
//   objectiveExtractor = <function1>,
//   α = 0.1,
//   iterations = 100
// )
```

Use the estimator

```scala
priceEstimator(RealtyListing(1416, 3, 2, 40, 0d))
// res0: Double = 288.60017635814035
```

Plot the error during the training

```scala
import axle.visualize._
import axle.algebra.Plottable._

val errorPlot = Plot(
  () => List(("error" -> priceEstimator.errTree)),
  connect = true,
  drawKey = true,
  colorOf = (label: String) => Color.black,
  title = Some("Linear Regression Error"),
  xAxis = Some(0d),
  xAxisLabel = Some("step"),
  yAxis = Some(0),
  yAxisLabel = Some("error"))
// errorPlot: Plot[String, Int, Double, collection.immutable.TreeMap[Int, Double]] = Plot(
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
//   colorOf = <function1>,
//   title = Some(value = "Linear Regression Error"),
//   keyTitle = None,
//   xAxis = Some(value = 0.0),
//   xAxisLabel = Some(value = "step"),
//   yAxis = Some(value = 0),
//   yAxisLabel = Some(value = "error")
// )

import axle.web._
import cats.effect._

errorPlot.svg[IO]("lrerror.svg").unsafeRunSync()
```

![lr error](/tutorial/images/lrerror.svg)
