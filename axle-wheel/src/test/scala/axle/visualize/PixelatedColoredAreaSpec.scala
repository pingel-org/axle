package axle.visualize

import org.scalatest._
import spire.algebra._
import cats.implicits._

class PixelatedColoredAreaSpec extends FunSuite with Matchers {

  test("PixelatedColoredArea renders a PNG") {

    def f(minX: Double, maxX: Double, minY: Double, maxY: Double) = minX + minY

    val n = 100

    // red to orange to yellow
    val roy = (0 until n).map(i => Color(255, ((i / n.toDouble) * 255).toInt, 0)).toArray

    def toColor(v: Double) = roy(v.toInt % n)

    val pca = PixelatedColoredArea(f, toColor, 400, 400, 0d, 1000d, 0d, 1000d)

    import axle.awt._

    val filename = "roy_diagonal.png"

    png(pca, filename)

    new java.io.File(filename).exists should be(true)
  }

  test("PixelatedColoredArea renders a Logistic Map") {

    import math.abs
    import axle.{ orbit, applyK }
    import axle.math.logisticMap

    def doubleClose(z: Double)(y: Double) = abs(z - y) < 1e-6

    import java.util.TreeSet
    val memo = collection.mutable.Map.empty[Double, TreeSet[Double]]

    implicit val ringDouble: Ring[Double] = spire.implicits.DoubleAlgebra
    def f(λ: Double, maxX: Double, maxY: Double, minY: Double): Boolean = {
      val f = logisticMap(λ)
      val set = memo.get(λ).getOrElse {
        val set = new TreeSet[Double]()
        orbit(f, applyK(f, 0.3, 100000), doubleClose) foreach { set.add }
        memo += λ -> set
        set
      }
      !set.tailSet(minY).headSet(maxY).isEmpty
    }

    import axle.visualize._

    val v2c = (v: Boolean) => if (v) Color.black else Color.white

    import cats.implicits._

    val pca = PixelatedColoredArea(f, v2c, 100, 100, 2.9, 4d, 0d, 1d)

    import axle.awt._

    val filename = "logMap.png"
    png(pca, filename)

    new java.io.File(filename).exists should be(true)
  }

}
