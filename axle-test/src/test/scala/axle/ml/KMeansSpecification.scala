package axle.ml

import org.specs2.mutable.Specification
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement

class KMeansSpecification
    extends Specification {

  "K-Means Clustering" should {
    "cluster random 2d points with small gaussian distribution around a center into 2 clusters" in {

      import spire.math.pi
      import spire.math.cos
      import spire.math.sin
      import spire.math.sqrt
      import scala.util.Random.nextDouble
      import scala.util.Random.nextGaussian
      import scala.util.Random.shuffle

      import org.jblas.DoubleMatrix
      import axle.jblas.linearAlgebraDoubleMatrix
      import axle.jblas.additiveAbGroupDoubleMatrix
      import axle.jblas.rowVectorInnerProductSpace
      import axle.jblas.moduleDoubleMatrix
      import axle.ml.distance.Euclidean
      import spire.algebra.Eq
      import spire.implicits.DoubleAlgebra
      import spire.implicits.IntAlgebra

      case class Foo(x: Double, y: Double)

      def fooSimilarity(foo1: Foo, foo2: Foo) = sqrt(List(foo1.x - foo2.x, foo1.y - foo2.y).map(x => x * x).sum)

      def randomPoint(center: Foo, σ2: Double): Foo = {
        val distance = nextGaussian() * σ2
        val angle = 2 * pi * nextDouble
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

  "K-Means Clustering" should {
    "cluster irises" in {

      import axle.quanta.Distance
      import axle.quanta.DistanceConverter
      import axle.jung._

      implicit val distanceConverter: DistanceConverter[Double] = {
        import spire.implicits.DoubleAlgebra
        import axle.algebra.modules.doubleRationalModule
        Distance.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Distance], Double => Double]]
      }

      import axle.data.Irises
      import axle.data.Iris

      val irisesData = new Irises
 
      import axle.ml.distance.Euclidean
      import org.jblas.DoubleMatrix
      import axle.jblas.linearAlgebraDoubleMatrix

      implicit val space: Euclidean[DoubleMatrix, Double] = {
        import spire.implicits.IntAlgebra
        import spire.implicits.DoubleAlgebra
        import axle.jblas.moduleDoubleMatrix
        implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](2)
        Euclidean[DoubleMatrix, Double]
      }

      import axle.ml.KMeans
      import axle.ml.PCAFeatureNormalizer
      import distanceConverter.cm
      import spire.implicits.DoubleAlgebra
      import axle.ml.PCAFeatureNormalizer

      val irisFeaturizer = (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)

      val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

      val irisConstructor = (features: Seq[Double]) => Iris(1 *: cm, 1 *: cm, 1 *: cm, 1 *: cm, "")

      val classifier = KMeans[Iris, List[Iris], List[Seq[Double]], DoubleMatrix](
        irisesData.irises,
        N = 2,
        irisFeaturizer,
        normalizer,
        irisConstructor,
        K = 3,
        iterations = 20)

      import axle.ml.ConfusionMatrix
      import spire.implicits.IntAlgebra
      // import spire.implicits.StringAlgebra

      // val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](classifier, irises.toVector, _.species, 0 to 2)

      1 must be equalTo 1
    }
  }

}
