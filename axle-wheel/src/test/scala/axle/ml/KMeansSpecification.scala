package axle.ml

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import org.jblas.DoubleMatrix

import cats.kernel.Eq
import cats.implicits._

import spire.algebra._
import spire.random.Generator.rng

import axle.shuffle

class KMeansSpecification
  extends FunSuite with Matchers {

  test("K-Means Clustering: cluster random 2d points with small gaussian distribution around a center into 2 clusters") {

    import spire.math.{pi, cos, sin, sqrt}
    import axle.jblas.linearAlgebraDoubleMatrix
    import axle.jblas.rowVectorInnerProductSpace
    import axle.algebra.distance.Euclidean

    case class Foo(x: Double, y: Double)

    def fooSimilarity(foo1: Foo, foo2: Foo) = sqrt(List(foo1.x - foo2.x, foo1.y - foo2.y).map(x => x * x).sum)

    def randomPoint(center: Foo, σ2: Double): Foo = {
      implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra
      val distance = rng.nextGaussian() * σ2
      val angle = 2 * pi * rng.nextDouble
      Foo(center.x + distance * cos(angle), center.y + distance * sin(angle))
    }

    val data = shuffle(
      (0 until 20).toList.map(i => randomPoint(Foo(100, 100), 0.1)) ++
        (0 until 30).toList.map(i => randomPoint(Foo(1, 1), 0.1)))(rng)
    //    ++ (0 until 25).map(i => randomPoint(Foo(1, 100), 0.1)))

    implicit val innerSpace = {
      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
      implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
      rowVectorInnerProductSpace[Int, Int, Double](2)
    }

    implicit val space = {
      implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
      new Euclidean[DoubleMatrix, Double]()
    }

    implicit val fooEq = Eq.fromUniversalEquals[Foo]

    implicit val la = {
      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
      axle.jblas.linearAlgebraDoubleMatrix[Double]
    }

    val km = KMeans(
      data,
      2,
      (p: Foo) => Seq(p.x, p.y),
      (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98),
      K = 2,
      100)(rng)

    val constructor = (features: Seq[Double]) => Foo(features(0), features(1))

    val exemplar = constructor(km.centroid(km(Foo(99.9, 99.9))))

    fooSimilarity(exemplar, Foo(100, 100)) should be < 5d
  }

  test("K-Means Clustering: cluster irises, generate confusion matrix, and create SVG visualization") {

    import axle.quanta.Distance
    import axle.quanta.DistanceConverter
    import axle.jung._

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

    implicit val distanceConverter: DistanceConverter[Double] = {
      import axle.algebra.modules.doubleRationalModule
      Distance.converterGraphK2[Double, DirectedSparseGraph]
    }

    import axle.data.Irises
    import axle.data.Iris

    val irisesData = new Irises

    import axle.algebra.distance.Euclidean
    import axle.jblas.linearAlgebraDoubleMatrix
    import axle.jblas.rowVectorInnerProductSpace

    implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

    implicit val space: Euclidean[DoubleMatrix, Double] = {
      implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
      implicit val inner = rowVectorInnerProductSpace[Int, Int, Double](2)
      new Euclidean[DoubleMatrix, Double]
    }

    import axle.ml.KMeans
    import axle.ml.PCAFeatureNormalizer
    import distanceConverter.cm

    val irisFeaturizer =
      (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)

    implicit val la = linearAlgebraDoubleMatrix[Double]

    val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

    val classifier: KMeans[Iris, List, DoubleMatrix] =
      KMeans[Iris, List, DoubleMatrix](
        irisesData.irises,
        N = 2,
        irisFeaturizer,
        normalizer,
        K = 3,
        iterations = 20)(rng)(
          Iris.irisEq,
          space,
          cats.Functor[List],
          la,
          axle.algebra.Indexed[List, Int],
          axle.algebra.Finite[List, Int])

    val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](
      classifier,
      irisesData.irises.toVector,
      _.species,
      0 to 2)

    import axle.visualize.Color._
    val colors = Vector(red, blue, green)

    import axle.visualize.KMeansVisualization
    val vis = KMeansVisualization(classifier, colors)

    import axle.web._
    val svgName = "kmeans.svg"
    svg(vis, svgName)

    import axle.awt._
    val pngName = "kmeans.png"
    png(vis, pngName)

    val plot = axle.visualize.Plot(
      () => classifier.distanceLogSeries,
      connect = true,
      drawKey = true,
      colorOf = colors,
      title = Some("KMeans Mean Centroid Distances"),
      xAxis = Some(0d),
      xAxisLabel = Some("step"),
      yAxis = Some(0),
      yAxisLabel = Some("average distance to centroid"))
    
    val plotSvgName = "kmeansvsiteration.svg"
    svg(plot, plotSvgName)

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
    new java.io.File(plotSvgName).exists should be(true)
    confusion.rowSums.columnSums.get(0, 0) should be(irisesData.irises.size)
    confusion.show should include("versicolor")
  }

}
