package axle.visualize

import java.awt.Color.black

import scala.Stream.continually
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
import axle.syntax.LinearAlgebraOps
import axle.syntax.linearalgebra._
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps

case class KMeansVisualization[D, F[_], M](
    classifier: KMeans[D, F, M],
    width: Int = 600,
    height: Int = 600,
    border: Int = 50,
    pointDiameter: Int = 10,
    fontName: String = "Courier New",
    fontSize: Int = 12) {

  import classifier.featureMatrix
  import classifier.la

  val colors = List(blue, red, green, orange, pink, yellow)

  val colorStream = continually(colors).flatten

  val maxs = featureMatrix.columnMaxs
  val mins = featureMatrix.columnMins

  val minX = mins.get(0, 0)
  val maxX = maxs.get(0, 0)
  val minY = mins.get(0, 1)
  val maxY = maxs.get(0, 1)

  implicit val ddls = axle.algebra.LengthSpace.doubleDoubleLengthSpace
  val scaledArea = ScaledArea2D(width, height, border, minX, maxX, minY, maxY)

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

  val points = (0 until classifier.K).zip(colorStream).flatMap {
    case (i, color) =>
      (0 until featureMatrix.rows) flatMap { r =>
        if (classifier.a.get(r, 0) === i) { // TODO avoid this scan
          // TODO figure out what to do when N > 2
          val center = Point2D(featureMatrix.get(r, 0), featureMatrix.get(r, 1))
          Some(Oval(scaledArea, center, pointDiameter, pointDiameter, color, color))
        } else {
          None
        }
      }
  }

}
