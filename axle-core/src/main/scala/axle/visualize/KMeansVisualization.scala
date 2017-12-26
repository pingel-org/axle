package axle.visualize

import axle.algebra.Tics
import axle.ml.KMeans
import axle.syntax.linearalgebra.matrixOps
import axle.visualize.Color.black
import axle.visualize.Color.darkGray
import axle.visualize.Color.white
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import axle.syntax.linearalgebra._
import cats.implicits._

case class KMeansVisualization[D, F, G, M](
  classifier:    KMeans[D, F, G, M],
  colorOf:       Int => Color,
  width:         Int                = 600,
  height:        Int                = 600,
  border:        Int                = 50,
  pointDiameter: Int                = 10,
  fontName:      String             = "Courier New",
  fontSize:      Int                = 12) {

  import classifier.featureMatrix
  import classifier.la

  val maxs = featureMatrix.columnMaxs
  val mins = featureMatrix.columnMins

  val minX = mins.get(0, 0)
  val maxX = maxs.get(0, 0)
  val minY = mins.get(0, 1)
  val maxY = maxs.get(0, 1)

  implicit val ddls = axle.algebra.LengthSpace.doubleDoubleLengthSpace
  val scaledArea =
    ScaledArea2D(
      border.toDouble, width - border.toDouble,
      border.toDouble, height - border.toDouble,
      minX, maxX,
      minY, maxY)

  implicit val doubleTics = Tics[Double]
  val xTics = XTics(scaledArea, doubleTics.tics(minX, maxX), fontName, fontSize.toDouble, bold = true, drawLines = true, Some(0d *: angleDouble.degree), black)
  val yTics = YTics(scaledArea, doubleTics.tics(minY, maxY), fontName, fontSize.toDouble, true, black)

  val boundingRectangle =
    Rectangle(scaledArea, Point2D(minX, minY), Point2D(maxX, maxY), borderColor = Some(black), fillColor = Some(white))

  def centroidOval(i: Int): Oval[Double, Double] = {
    val denormalized = classifier.normalizer.unapply(classifier.μ.row(i))
    val center = Point2D(denormalized(0), denormalized(1))
    Oval(scaledArea, center, 3 * pointDiameter, 3 * pointDiameter, colorOf(i), darkGray)
  }

  val centroidOvals = (0 until classifier.K).map(centroidOval)

  val points = (0 until featureMatrix.rows).map { r =>
    val clusterNumber = classifier.a.get(r, 0).toInt
    val color = colorOf(clusterNumber)
    // TODO figure out what to do when N > 2
    val center = Point2D(featureMatrix.get(r, 0), featureMatrix.get(r, 1))
    Oval(scaledArea, center, pointDiameter, pointDiameter, color, color)
  }

}
