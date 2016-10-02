package axle.ml

import org.specs2.mutable._
import axle.jblas._

class FeatureNormalizerSpec extends Specification {

  "identity normalizer" should {
    "leave features alone" in {

      import spire.implicits.DoubleAlgebra
      implicit val la = linearAlgebraDoubleMatrix[Double]

      val X = la.matrix(
        4,
        2,
        List(1.1d, 2d, 3d, 4.1d, 5.2d, 6.3d, 7d, 8d).toArray)

      val featureNormalizer = IdentityFeatureNormalizer(X)

      val normalized = featureNormalizer(1.4 :: 6.7 :: Nil)

      normalized must be equalTo la.matrix(1, 2, Array(1.4, 6.7))
    }
  }

  "z score normalizer" should {
    "center features" in {

      import spire.implicits.DoubleAlgebra
      implicit val la = linearAlgebraDoubleMatrix[Double]

      val X = la.matrix(
        4,
        2,
        List(1.1d, 2d, 3d, 4.1d, 5.2d, 6.3d, 7d, 8d).toArray)

      val featureNormalizer = ZScoreFeatureNormalizer(X)

      val normalized = featureNormalizer(1.4 :: 6.7 :: Nil)

      normalized must be equalTo la.matrix(1, 2, Array(-0.726598, 0.051956))
    }
  }

}