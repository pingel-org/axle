package axle.ml

import org.specs2.mutable._
import util.Random
import math.{ Pi, cos, sin }

class KMeansSpecification extends Specification {

  "K-Means Clustering" should {
    "work" in {

      import KMeans._

      case class Foo(x: Double, y: Double)

      def fooSimilarity(foo1: Foo, foo2: Foo) = List(foo1.x - foo2.x, foo1.y - foo2.y).map(x => x * x).sum

      def randomPoint(center: Foo, σ2: Double): Foo = {
        val distance = Random.nextGaussian() * σ2
        val angle = 2 * Pi * Random.nextDouble
        Foo(center.x + distance * cos(angle), center.y + distance * sin(angle))
      }

      val data = Random.shuffle(
        (0 until 20).map(i => randomPoint(Foo(15, 15), 1.0)) ++
          (0 until 30).map(i => randomPoint(Foo(5, 15), 1.0)) ++
          (0 until 25).map(i => randomPoint(Foo(15, 5), 1.0)))

      val classifier = cluster(
        data, 2,
        (p: Foo) => List(p.x, p.y),
        (features: List[Double]) => Foo(features(0), features(1)),
        3,
        100)

      fooSimilarity(classifier.exemplar(classifier.classify(Foo(14, 14))), Foo(15, 15)) must be lessThan 1.0
    }
  }

}
