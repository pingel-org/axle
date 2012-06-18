
import scala.util.Random
import scala.math.{ Pi, cos, sin }
import axle.ml.KMeans._
import axle.visualize._

case class Foo(x: Double, y: Double)

def randomFoo(center: Foo, σ2: Double): Foo = {
  val distance = Random.nextGaussian() * σ2
  val angle = 2 * Pi * Random.nextDouble
  Foo(center.x + distance * cos(angle), center.y + distance * sin(angle))
}

val center1 = Foo(15, 15)
val center2 = Foo(5, 15)
val center3 = Foo(15, 5)

val data = Random.shuffle(
  (0 until 20).map(i => randomFoo(center1, 1.0)) ++
  (0 until 30).map(i => randomFoo(center2, 1.0)) ++
  (0 until 25).map(i => randomFoo(center3, 1.0)))

println("data.length = " + data.length)

val classifier = cluster(
  data,
  2, // numFeatures
  (p: Foo) => List(p.x, p.y),
  (features: List[Double]) => new Foo(features(0), features(1)),
  3, // K
  100) // iterations

val frame = new AxleFrame()
//frame.add(new KMeansVisualization(classifier))

val plot = new Plot(classifier.distanceLogTreeMaps, true)
frame.add(plot)
