package axle.visualize

import java.awt.Color.black
import java.awt.Color.blue
import java.awt.Color.darkGray
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Font

import axle.algebra.LinearAlgebra
import axle.algebra.Tics
import axle.ml.KMeans
import axle.syntax.linearalgebra.matrixOps
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.implicits.DoubleAlgebra

case class KMeansVisualization[D, F[_], M](
    classifier: KMeans[D, F, M],
    w: Int = 600,
    h: Int = 600,
    border: Int = 50,
    pointDiameter: Int = 10,
    fontName: String = "Courier New",
    fontSize: Int = 12)(
        implicit la: LinearAlgebra[M, Int, Int, Double]) {

  import classifier.featureMatrix

  val colors = List(blue, red, green, orange, pink, yellow)

  val maxs = featureMatrix.columnMaxs
  val mins = featureMatrix.columnMins

  val minX = mins.get(0, 0)
  val maxX = maxs.get(0, 0)
  val minY = mins.get(0, 1)
  val maxY = maxs.get(0, 1)

  implicit val ddls = axle.algebra.LengthSpace.doubleDoubleLengthSpace
  val scaledArea = ScaledArea2D(w, h, border, minX, maxX, minY, maxY)

  val normalFont = new Font(fontName, Font.BOLD, fontSize)

  implicit val doubleTics = Tics[Double]
  val xTics = XTics(scaledArea, doubleTics.tics(minX, maxX), normalFont, true, 0d *: angleDouble.degree, black)
  val yTics = YTics(scaledArea, doubleTics.tics(minY, maxY), normalFont, black)

  val boundingRectangle = Rectangle(scaledArea, Point2D(minX, minY), Point2D(maxX, maxY), borderColor = Some(black))

  def centroidOval(i: Int): Oval[Double, Double] = {
    val denormalized = classifier.normalizer.unapply(classifier.μ.row(i))
    val center = Point2D(denormalized(0), denormalized(1))
    Oval(scaledArea, center, 3 * pointDiameter, 3 * pointDiameter, colors(i % colors.length), darkGray)
  }

  val centroidOvals = (0 until classifier.K).map(centroidOval)

}
