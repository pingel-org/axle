package axle.ml

import org.specs2.mutable._
import util.Random.{ shuffle, nextGaussian, nextDouble }
import math.{ Pi, cos, sin, sqrt }
import axle.matrix.JblasMatrixModule
import axle.ml.distance._
import spire.algebra._

class KMeansSpecification extends Specification {

  "K-Means Clustering" should {
    "work" in {

      import KMeansModule._

      case class Foo(x: Double, y: Double)

      def fooSimilarity(foo1: Foo, foo2: Foo) = sqrt(List(foo1.x - foo2.x, foo1.y - foo2.y).map(x => x * x).sum)

      def randomPoint(center: Foo, σ2: Double): Foo = {
        val distance = nextGaussian() * σ2
        val angle = 2 * Pi * nextDouble
        Foo(center.x + distance * cos(angle), center.y + distance * sin(angle))
      }

      val data = shuffle(
        (0 until 20).map(i => randomPoint(Foo(100, 100), 0.1)) ++
          (0 until 30).map(i => randomPoint(Foo(1, 1), 0.1)))
      //    ++ (0 until 25).map(i => randomPoint(Foo(1, 100), 0.1)))

      implicit val space = new axle.ml.distance.Euclidian(2)

      implicit val fooEq = new Eq[Foo] {
        def eqv(x: Foo, y: Foo): Boolean = x equals y
      }

      val km = classifier(
        data,
        2,
        (p: Foo) => List(p.x, p.y),
        (features: Seq[Double]) => Foo(features(0), features(1)),
        K = 2,
        100)

      val exemplar = km.exemplar(km(Foo(99.9, 99.9)))

      fooSimilarity(exemplar, Foo(100, 100)) must be lessThan 5d
    }
  }

}
