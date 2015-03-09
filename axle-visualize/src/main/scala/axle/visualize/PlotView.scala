package axle.visualize

import java.awt.Color.black
import java.awt.Font

import scala.Stream.continually

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Eq
import axle.algebra.DirectedGraph
import axle.quanta.UnitOfMeasurement
import axle.quanta.Angle

case class PlotView[X, Y, D, DG[_, _]: DirectedGraph](plot: Plot[X, Y, D], data: Seq[(String, D)], normalFont: Font)(
  implicit xZero: Zero[X], xts: Tics[X], xEq: Eq[X], xLength: LengthSpace[X, _],
  yZero: Zero[Y], yts: Tics[Y], yEq: Eq[Y], yLength: LengthSpace[Y, _],
  angleCg: DG[UnitOfMeasurement[Angle, Double], Double => Double]) {

  import plot._

  val colorStream = continually(colors.toStream).flatten

  val keyOpt = if (drawKey) {
    Some(Key(plot, normalFont, colorStream, keyWidth, keyTopPadding, data))
  } else {
    None
  }

  val (minX, maxX) = plotDataView.xRange(data, yAxis)
  val (minY, maxY) = plotDataView.yRange(data, xAxis)

  val minPoint = Point2D(minX, minY)
  val maxPoint = Point2D(maxX, maxY)

  val scaledArea = ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height, border,
    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y)

  val vLine = VerticalLine(scaledArea, yAxis.getOrElse(minX), black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(minY), black)
  val xTics = XTics(scaledArea, xts.tics(minX, maxX), normalFont, true, 0 *: Angle.metadata[Double].degree, black)
  val yTics = YTics(scaledArea, yts.tics(minY, maxY), normalFont, black)

  val dataLines = DataLines(scaledArea, data, plotDataView.xsOf, plotDataView.valueOf, colorStream, pointDiameter, connect)

}
