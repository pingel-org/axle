
import scala.util.Random
import scala.math.{ Pi, cos, sin }
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
  (0 until 30).map(i => randomFoo(Foo( 5, 15), 2.0)) ++
  (0 until 25).map(i => randomFoo(Foo(15,  5), 2.0)) ++
  (0 until 25).map(i => randomFoo(Foo(12,  6), 2.0)) ++
  (0 until 25).map(i => randomFoo(Foo( 4, 10), 2.0)) ++
  (0 until 25).map(i => randomFoo(Foo( 8,  3), 2.0)))

val classifier = cluster(
  data,
  2,   // numFeatures
  (p: Foo) => List(p.x, p.y),
  (features: List[Double]) => new Foo(features(0), features(1)),
  6,   // K
  100) // iterations

new AxleFrame().add(new KMeansVisualization(classifier))

new AxleFrame().add(new Plot[Int, Int, Double, Double](classifier.distanceLogSeries(), true,
    title = Some("KMeans Centroid Distances"),
    xAxis=0.0, xAxisLabel=Some("step"), yAxis=0, yAxisLabel=Some("total distance to centroid")))
