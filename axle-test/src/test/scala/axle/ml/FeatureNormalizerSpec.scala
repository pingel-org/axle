package axle.ml

import org.scalatest._
import axle.jblas._

class FeatureNormalizerSpec extends FunSuite with Matchers {

  import org.scalactic._

  import scala.reflect.ClassTag
  implicit def catsToScalacticEq[T](implicit ckeq: cats.kernel.Eq[T], ct: ClassTag[T]): Equality[T] =
    new Equality[T] {
      def areEqual(a: T, b: Any): Boolean =
        ct.runtimeClass.isInstance(b) && ckeq.eqv(a, b.asInstanceOf[T])
    }

  test("identity normalizer leaves features alone") {

    import spire.implicits.DoubleAlgebra
    implicit val la = linearAlgebraDoubleMatrix[Double]

    val X = la.fromColumnMajorArray(
      4,
      2,
      List(1.1d, 2d, 3d, 4.1d, 5.2d, 6.3d, 7d, 8d).toArray)

    val featureNormalizer = IdentityFeatureNormalizer(X)

    val normalized = featureNormalizer(1.4 :: 6.7 :: Nil)

    normalized should be(la.fromColumnMajorArray(1, 2, Array(1.4, 6.7)))
  }

  test("z score normalizer centers features") {

    import spire.implicits.DoubleAlgebra
    implicit val la = linearAlgebraDoubleMatrix[Double]

    val X = la.fromColumnMajorArray(
      4,
      2,
      List(1.1d, 2d, 3d, 4.1d, 5.2d, 6.3d, 7d, 8d).toArray)

    val featureNormalizer = ZScoreFeatureNormalizer(X)

    val normalized = featureNormalizer(1.4 :: 6.7 :: Nil)

    val expected = la.fromColumnMajorArray(1, 2, Array(-0.726597627147548, 0.051956328853936716))

    normalized should ===(expected)
  }

}
