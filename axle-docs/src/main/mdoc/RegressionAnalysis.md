# Regression Analysis

## Linear Regression

`axle.ml.LinearRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Linear Regression](https://en.wikipedia.org/wiki/Linear_regression)

### Example: Home Prices

```scala mdoc:silent
case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

val listings = List(
  RealtyListing(2104, 5, 1, 45, 460d),
  RealtyListing(1416, 3, 2, 40, 232d),
  RealtyListing(1534, 3, 2, 30, 315d),
  RealtyListing(852, 2, 1, 36, 178d))
```

Create a price estimator using linear regression.

```scala mdoc:silent
import cats.implicits._
import spire.algebra.Rng
import spire.algebra.NRoot
import axle.jblas._

implicit val rngDouble: Rng[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra

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

```scala mdoc
priceEstimator(RealtyListing(1416, 3, 2, 40, 0d))
```

Create a Plot of the error during the training

```scala mdoc:silent
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
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

errorPlot.svg[IO]("@DOCWD@/images/lrerror.svg").unsafeRunSync()
```

![lr error](/images/lrerror.svg)

## Logistic Regression

`axle.ml.LogisticRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Logistic Regression](https://en.wikipedia.org/wiki/Logistic_regression)

### Example: Test Pass Probability

Predict Test Pass Probability as a Function of Hours Studied

```scala mdoc:silent:reset
case class Student(hoursStudied: Double, testPassed: Boolean)

val data = List(
  Student(0.50, false),
  Student(0.75, false),
  Student(1.00, false),
  Student(1.25, false),
  Student(1.50, false),
  Student(1.75, false),
  Student(1.75, true),
  Student(2.00, false),
  Student(2.25, true),
  Student(2.50, false),
  Student(2.75, true),
  Student(3.00, false),
  Student(3.25, true),
  Student(3.50, false),
  Student(4.00, true),
  Student(4.25, true),
  Student(4.50, true),
  Student(4.75, true),
  Student(5.00, true),
  Student(5.50, true)
)
```

Create a test pass probability function using logistic regression.

```scala mdoc
import axle.jblas._
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]

import axle.ml.LogisticRegression

val featureExtractor = (s: Student) => (s.hoursStudied :: Nil)

val objectiveExtractor = (s: Student) => s.testPassed

val pTestPass = LogisticRegression(
  data,
  1,
  featureExtractor,
  objectiveExtractor,
  0.1,
  10)
```

Use the estimator

```scala
testPassProbability(2d :: Nil)
```

(Note: The implementation is incorrect, so the result is elided until the error is fixed)

## Future Work

Fix Logistic Regression
