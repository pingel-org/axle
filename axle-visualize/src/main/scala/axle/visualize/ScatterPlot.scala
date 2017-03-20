package axle.visualize

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.visualize.Color.black
import axle.visualize.element._
import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

/**
 * labelOf optionally returns a double:
 *
 * 1. The label of the given data point
 *
 * 2. A Boolean representing whether that label should be permanently displayed
 *    vs. just shown as a tooltip/mouseover
 *
 */

case class ScatterPlot[S, X: Eq: Tics: Order, Y: Eq: Tics: Order, D](
    data: D,
    width: Double = 600d,
    height: Double = 600d,
    border: Double = 50d,
    diameterOf: (D, X, Y) => Double = (d: D, x: X, y: Y) => 10d,
    colorOf: (D, X, Y) => Color,
    labelOf: (D, X, Y) => Option[(S, Boolean)],
    fontName: String = "Courier New",
    fontSize: Double = 12d,
    bold: Boolean = false,
    titleFontName: String = "Palatino",
    titleFontSize: Double = 20d,
    title: Option[String] = None,
    drawXTics: Boolean = true,
    drawXTicLines: Boolean = true,
    drawYTics: Boolean = true,
    drawYTicLines: Boolean = true,
    drawBorder: Boolean = true,
    xRange: Option[(X, X)] = None,
    yAxis: Option[X] = None,
    yRange: Option[(Y, Y)] = None,
    xAxis: Option[Y] = None,
    xAxisLabel: Option[String] = None,
    yAxisLabel: Option[String] = None)(
        implicit val lengthX: LengthSpace[X, X, Double],
        val lengthY: LengthSpace[Y, Y, Double],
        val dataView: ScatterDataView[X, Y, D],
        showS: Show[S]) {

  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2, height - border / 2, fontName, fontSize, bold = true))

  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2, fontName, fontSize, bold = true, angle = Some(90d *: angleDouble.degree)))

  val titleText = title.map(Text(_, width / 2, titleFontSize, titleFontName, titleFontSize, bold = true))

  def minMax[T: Ordering](data: List[T]): (T, T) = (data.min, data.max)

  val domain = dataView.dataToDomain(data)

  val (minX, maxX) = xRange.getOrElse(minMax(yAxis.toList ++ domain.map(_._1).toList))
  val (minY, maxY) = yRange.getOrElse(minMax(xAxis.toList ++ domain.map(_._2).toList))

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

  val dataPoints = DataPoints(scaledArea, data, diameterOf, colorOf, labelOf)

}
