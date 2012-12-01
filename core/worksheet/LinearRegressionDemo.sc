
object lrO {

  import collection._
  import axle.ml.LinearRegression._
  import axle.visualize._
  import axle.visualize.Plottable._

  case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

  val data = RealtyListing(2104, 5, 1, 45, 460.0) :: RealtyListing(1416, 3, 2, 40, 232.0) :: RealtyListing(1534, 3, 2, 30, 315.0) :: RealtyListing(852, 2, 1, 36, 178.0) :: Nil

  val estimator = regression(
    data,
    numFeatures = 4,
    featureExtractor = (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
    objectiveExtractor = (rl: RealtyListing) => rl.price,
    α = 0.1,
    iterations = 100)

  val priceGuess = estimator.estimate(RealtyListing(1416, 3, 2, 40, 0.0))

  val errorPlot = Plot(lfs = List(("error" -> estimator.errTree)),
    connect = true,
    drawKey = true,
    title = Some("Linear Regression Error"),
    xAxis = 0.0,
    xAxisLabel = Some("step"),
    yAxis = 0,
    yAxisLabel = Some("error"))
  
  show(errorPlot)

}