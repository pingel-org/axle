---
layout: page
title: Linear Regression
permalink: /tutorial/linear_regression/
---

`axle.ml.LinearRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Linear Regression](https://en.wikipedia.org/wiki/Linear_regression)

Predicting Home Prices
----------------------

```tut:book
case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

val listings = List(
  RealtyListing(2104, 5, 1, 45, 460d),
  RealtyListing(1416, 3, 2, 40, 232d),
  RealtyListing(1534, 3, 2, 30, 315d),
  RealtyListing(852, 2, 1, 36, 178d))
```

Create a price estimator using linear regression.

```tut:book
import cats.implicits._
import spire.implicits.DoubleAlgebra
import axle.jblas._

implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]

import axle.ml.LinearRegression

val priceEstimator = LinearRegression(
  listings,
  numFeatures = 4,
  featureExtractor = (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
  objectiveExtractor = (rl: RealtyListing) => rl.price,
  α = 0.1,
  iterations = 100)
```

Use the estimator

```tut:book
priceEstimator(RealtyListing(1416, 3, 2, 40, 0d))
```

Plot the error during the training

```tut:book
import spire.implicits._
import axle.visualize._
import axle.algebra.Plottable._
import axle.eqTreeMap

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

import axle.web._
svg(errorPlot, "lrerror.svg")
```

![lr error](/tutorial/images/lrerror.svg)
