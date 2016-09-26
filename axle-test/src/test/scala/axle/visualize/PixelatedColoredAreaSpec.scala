package axle.visualize

import org.specs2.mutable.Specification
import spire.implicits.DoubleAlgebra

class PixelatedColoredAreaSpec extends Specification {

  "PixelatedColoredArea" should {
    "render a PNG" in {

      def f(x: Double, y: Double) = x + y

      val n = 100

      // red to orange to yellow
      val roy = (0 until n).map(i => Color(255, ((i / n.toDouble) * 255).toInt, 0)).toArray

      def toColor(v: Double) = roy(v.toInt % n)

      val pca = PixelatedColoredArea(f, toColor, 400, 400, 0d, 1000d, 0d, 1000d)

      import axle.awt._

      val filename = "roy_diagonal.png"

      png(pca, filename)

      new java.io.File(filename).exists must be equalTo true
    }
  }

}