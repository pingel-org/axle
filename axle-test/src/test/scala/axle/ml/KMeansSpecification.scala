package axle.ml

import scala.math.Pi
import scala.math.cos
import scala.math.sin
import scala.math.sqrt
import scala.util.Random.nextDouble
import scala.util.Random.nextGaussian
import scala.util.Random.shuffle

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification

import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.additiveAbGroupDoubleMatrix
import axle.jblas.rowVectorInnerProductSpace
import axle.jblas.moduleDoubleMatrix
import axle.ml.distance.Euclidean
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra

class KMeansSpecification
    extends Specification {

  "K-Means Clustering" should {
    "work" in {

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

      implicit val innerSpace = rowVectorInnerProductSpace[Int, Int, Double](2)

      implicit val space = Euclidean[DoubleMatrix, Double]()

      implicit val fooEq = new Eq[Foo] {
        def eqv(x: Foo, y: Foo): Boolean = x equals y
      }

      val km = KMeans(
        data,
        2,
        (p: Foo) => Seq(p.x, p.y),
        (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98),
        (features: Seq[Double]) => Foo(features(0), features(1)),
        K = 2,
        100)

      val exemplar = km.exemplar(km(Foo(99.9, 99.9)))

      fooSimilarity(exemplar, Foo(100, 100)) must be lessThan 5d
    }
  }

}
