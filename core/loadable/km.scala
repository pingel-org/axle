
object kmO {

  import util.Random
  import math.{ Pi, cos, sin }
  import axle.ml.KMeans._
  import axle.visualize._

  case class Foo(x: Double, y: Double)

  def randomFoo(center: Foo, σ2: Double) = {
    val distance = Random.nextGaussian() * σ2
    val angle = 2 * Pi * Random.nextDouble
    Foo(center.x + distance * cos(angle), center.y + distance * sin(angle))
  }

  val data = Random.shuffle(
    (0 until 20).map(i => randomFoo(Foo(15, 15), 2.0)) ++
      (0 until 30).map(i => randomFoo(Foo(5, 15), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(15, 5), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(12, 6), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(4, 10), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(8, 3), 2.0)))

  val classifier = cluster(
    data,
    N = 2,
    featureExtractor = (p: Foo) => List(p.x, p.y),
    constructor = (features: List[Double]) => Foo(features(0), features(1)),
    K = 6,
    iterations = 100)

  val plot = Plot(classifier.averageDistanceLogSeries(),
    connect = true,
    drawKey = true,
    title = Some("KMeans Mean Centroid Distances"),
    xAxis = 0.0,
    xAxisLabel = Some("step"),
    yAxis = 0,
    yAxisLabel = Some("average distance to centroid"))

  show(classifier)
  show(plot)

}
