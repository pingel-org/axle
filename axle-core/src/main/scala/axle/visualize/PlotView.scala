package axle.visualize

import axle.visualize.Color.black
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics

case class PlotView[S, X, Y, D](
  plot: Plot[S, X, Y, D],
  data: Seq[(S, D)]) {

  import plot._

  val keyOpt = if (drawKey) {
    Some(Key(plot, keyTitle, colorOf, keyWidth, keyTopPadding, data))
  } else {
    None
  }

  val (minX, maxX) = plotDataView.xRange(data, yAxis)
  val (minY, maxY) = plotDataView.yRange(data, xAxis)

  val minPoint = Point2D(minX, minY)
  val maxPoint = Point2D(maxX, maxY)

  val scaledArea = ScaledArea2D(
    border.toDouble, // left
    (if (drawKey) width - (keyWidth + keyLeftPadding) else width) - border.toDouble, // right
    border.toDouble, // top -- Note top/bottom is counter-intuitively in reverse order from minY/maxY
    height - border.toDouble, // bottom
    minPoint.x, // minX
    maxPoint.x, // maxX
    minPoint.y, // minY
    maxPoint.y  // maxY
  )

  val vLine = VerticalLine(scaledArea, yAxis.getOrElse(minX), black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(minY), black)
  val xTics = XTics(scaledArea, xts.tics(minX, maxX), fontName, fontSize.toDouble, bold = true, drawLines = true, Some(0d *: angleDouble.degree), black)
  val yTics = YTics(scaledArea, yts.tics(minY, maxY), fontName, fontSize.toDouble, true, black)

  val dataLines = DataLines(scaledArea, data, plotDataView.xsOf, plotDataView.valueOf, colorOf, pointDiameter, connect)

}
