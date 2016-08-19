Linear Regression
=================

`axle.ml.LinearRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on <a href="https://en.wikipedia.org/wiki/Linear_regression">Linear Regression</a>

Predicting Home Prices
----------------------

```scala
scala> case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)
defined class RealtyListing

scala> val listings = List(
     |   RealtyListing(2104, 5, 1, 45, 460d),
     |   RealtyListing(1416, 3, 2, 40, 232d),
     |   RealtyListing(1534, 3, 2, 30, 315d),
     |   RealtyListing(852, 2, 1, 36, 178d))
listings: List[RealtyListing] = List(RealtyListing(2104.0,5,1,45,460.0), RealtyListing(1416.0,3,2,40,232.0), RealtyListing(1534.0,3,2,30,315.0), RealtyListing(852.0,2,1,36,178.0))
```

Create a price estimator using linear regression.

```scala
scala> import axle.jblas._
import axle.jblas._

scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
laJblasDouble: axle.algebra.LinearAlgebra[org.jblas.DoubleMatrix,Int,Int,Double] = axle.jblas.package$$anon$12@4ed99399

scala> import axle.ml.LinearRegression
import axle.ml.LinearRegression

scala> val estimator = LinearRegression(
     |   listings,
     |   numFeatures = 4,
     |   featureExtractor = (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
     |   objectiveExtractor = (rl: RealtyListing) => rl.price,
     |   α = 0.1,
     |   iterations = 100)
estimator: axle.ml.LinearRegression[RealtyListing,org.jblas.DoubleMatrix] = LinearRegression(List(RealtyListing(2104.0,5,1,45,460.0), RealtyListing(1416.0,3,2,40,232.0), RealtyListing(1534.0,3,2,30,315.0), RealtyListing(852.0,2,1,36,178.0)),4,<function1>,<function1>,0.1,100)
```

Use the estimator

```scala
scala> estimator.estimate(RealtyListing(1416, 3, 2, 40, 0d))
res0: Double = 288.60017635814035
```

Plot the error during the training

```scala
scala> import spire.implicits._
import spire.implicits._

scala> import axle.visualize._
import axle.visualize._

scala> import axle.algebra.Plottable._
import axle.algebra.Plottable._

scala> val errorPlot = Plot(
     |   List(("error" -> estimator.errTree)),
     |   connect = true,
     |   drawKey = true,
     |   title = Some("Linear Regression Error"),
     |   xAxis = Some(0d),
     |   xAxisLabel = Some("step"),
     |   yAxis = Some(0),
     |   yAxisLabel = Some("error"))
errorPlot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((error,Map(0 -> 2.5128090091314887, 1 -> 1.9940009164767303, 2 -> 1.58191872192217, 3 -> 1.2545993429121414, 4 -> 0.9945998668072344, 5 -> 0.7880672508058177, 6 -> 0.623999488249853, 7 -> 0.493658835327345, 8 -> 0.39010580239065656, 9 -> 0.30782905514262204, 10 -> 0.24245148633625785, 11 -> 0.19049678122062969, 12 -> 0.14920402642498015, 13 -> 0.11638047439014737, 14 -> 0.09028461049827027, 15 -> 0.06953328625861772, 16 -> 0.05302796548021301, 17 -> 0.03989614976064845, 18 -> 0.02944485921520371, 19 -> 0.021123687341158864, 20 -> 0.01449545955216976, 21 -> 0.009212930461954393, 22 -> 0.005000277075209034, 23 -> 0.0016384008348354767, 24 -> 0.0010467453790410433, 25 -> 0.0031934278662631...

scala> import axle.web._
import axle.web._

scala> svg(errorPlot, "lrerror.svg")
```

![lr error](../images/lrerror.svg)
