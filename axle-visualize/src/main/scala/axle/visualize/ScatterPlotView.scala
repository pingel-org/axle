package axle.visualize

import axle.visualize.Color.black
import axle.visualize.element.HorizontalLine
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics

case class ScatterPlotView[X, Y, D](
    plot: ScatterPlot[X, Y, D],
    data: D) {

  import plot._

  val (minX, maxX) = dataView.xRange(data, yAxis)
  val (minY, maxY) = dataView.yRange(data, xAxis)

  val minPoint = Point2D(minX, minY)
  val maxPoint = Point2D(maxX, maxY)

  val scaledArea = ScaledArea2D(
    width = width,
    height,
    border,
    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y)

  val vLine = VerticalLine(scaledArea, yAxis.getOrElse(minX), black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(minY), black)
  val xTics = XTics(scaledArea, xts.tics(minX, maxX), fontName, fontSize, bold = true, drawLines = true, 0d *: angleDouble.degree, black)
  val yTics = YTics(scaledArea, yts.tics(minY, maxY), fontName, fontSize, true, black)

  // val dataLines = DataLines(scaledArea, data, plotDataView.xsOf, plotDataView.valueOf, colorStream, pointDiameter, connect)

}