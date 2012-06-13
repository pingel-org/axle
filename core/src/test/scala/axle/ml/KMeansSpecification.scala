package axle.ml

import org.specs2.mutable._
import scala.util.Random
import scala.math.{ Pi, cos, sin }

class KMeansSpecification extends Specification {

  "K-Means Clustering" should {
    "work" in {

      import KMeans._

      case class Point(x: Double, y: Double)

      def randomPoint(center: Point, σ2: Double): Point = {
        val distance = Random.nextGaussian() * σ2
        val angle = 2 * Pi * Random.nextDouble
        Point(center.x + distance * cos(angle), center.y + distance * sin(angle))
      }

      val center1 = Point(15, 15)
      val center2 = Point(5, 15)
      val center3 = Point(15, 5)

      val data = Random.shuffle(
        (0 until 20).map(i => randomPoint(center1, 1.0)) ++
          (0 until 30).map(i => randomPoint(center2, 1.0)) ++
          (0 until 25).map(i => randomPoint(center3, 1.0)))

      val classifier = cluster(
        data, 2,
        (p: Point) => List(p.x, p.y),
        (features: List[Double]) => new Point(features(0), features(1)),
        3, 100)

      val closestIndex = classifier.classify(new Point(14, 14))
      val closest = classifier.exemplar(closestIndex)

      // TODO: assertions
      closest must be equalTo (Point(15, 15))
    }
  }

}
