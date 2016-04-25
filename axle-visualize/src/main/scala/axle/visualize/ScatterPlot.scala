package axle.visualize

import scala.Stream.continually

import axle.algebra.Tics
import axle.algebra.LengthSpace
import axle.ml.KMeans
import axle.syntax.linearalgebra.matrixOps
import axle.visualize.Color.black
import axle.visualize.Color.darkGray
import axle.visualize.Color.white
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import axle.syntax.LinearAlgebraOps
import axle.syntax.linearalgebra._
import spire.implicits.DoubleAlgebra
import spire.algebra.Eq
import spire.algebra.Order
import spire.compat.ordering
import spire.implicits.eqOps
import axle.visualize.element.Text
import axle.visualize.element.HorizontalLine
import axle.visualize.element.VerticalLine
import axle.visualize.element.DataPoints

case class ScatterPlot[X: Eq: Tics: Order, Y: Eq: Tics: Order](
    data: Set[(X, Y)],
    width: Int = 600,
    height: Int = 600,
    border: Int = 50,
    pointDiameter: Int = 10,
    fontName: String = "Courier New",
    fontSize: Int = 12,
    bold: Boolean = false,
    titleFontName: String = "Palatino",
    titleFontSize: Int = 20,
    colors: Seq[Color] = defaultColors,
    title: Option[String] = None,
    drawXTics: Boolean = true,
    drawXTicLines: Boolean = true,
    drawYTics: Boolean = true,
    drawYTicLines: Boolean = true,
    drawBorder: Boolean = true,
    xAxis: Option[Y] = None,
    xAxisLabel: Option[String] = None,
    yAxis: Option[X] = None,
    yAxisLabel: Option[String] = None)(
        implicit lengthX: LengthSpace[X, X, Double],
        lengthY: LengthSpace[Y, Y, Double]) {

  val colorStream = continually(colors).flatten

  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2, height - border / 2, fontName, fontSize, bold = true))

  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2, fontName, fontSize, bold = true, angle = Some(90d *: angleDouble.degree)))

  val titleText = title.map(Text(_, width / 2, titleFontSize, titleFontName, titleFontSize, bold = true))

  def minMax[T: Ordering](data: List[T]): (T, T) = (data.min, data.max)

  val (minX, maxX) = minMax(yAxis.toList ++ data.map(_._1).toList)
  val (minY, maxY) = minMax(xAxis.toList ++ data.map(_._2).toList)

  val minPoint = Point2D(minX, minY)
  val maxPoint = Point2D(maxX, maxY)

  val scaledArea = ScaledArea2D(
    width = width,
    height, border,
    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y)

  val vLine = VerticalLine(scaledArea, yAxis.getOrElse(minX), black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(minY), black)
  val xTics = XTics(scaledArea, Tics[X].tics(minX, maxX), fontName, fontSize, bold = true, drawLines = drawXTicLines, 0d *: angleDouble.degree, black)
  val yTics = YTics(scaledArea, Tics[Y].tics(minY, maxY), fontName, fontSize, drawLines = drawYTicLines, black)

  val dataPoints = DataPoints(scaledArea, data, colorStream, pointDiameter)

}
