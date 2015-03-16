package axle.visualize

import java.awt.Color
import java.awt.Color.black
import java.awt.Font

import axle.Show
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.quanta.AngleConverter
import axle.string
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Eq
import spire.algebra.Order
import spire.compat.ordering
import spire.implicits.DoubleAlgebra

case class BarChartView[S: Show, Y: Plottable: Order: Eq: Tics, D](
  chart: BarChart[S, Y, D],
  data: D,
  colorStream: Stream[Color],
  normalFont: Font)(
    implicit yLength: LengthSpace[Y, _], angleMeta: AngleConverter[Double]) {

  import chart._

  val minX = 0d
  val maxX = 1d
  val yAxis = minX

  val slices = chart.dataView.keys(data)

  val padding = 0.05 // on each side
  val widthPerSlice = (1d - (2 * padding)) / slices.size
  val whiteSpace = widthPerSlice * (1d - barWidthPercent)

  val (dataMinY, dataMaxY) = dataView.yRange(data)
  val minY = List(xAxis, dataMinY).min
  val maxY = List(xAxis, dataMaxY).max

  implicit val ddls = axle.algebra.LengthSpace.doubleDoubleLengthSpace

  val scaledArea = ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY)

  val vLine = VerticalLine(scaledArea, yAxis, black)
  val hLine = HorizontalLine(scaledArea, xAxis, black)

  val gTics = XTics(
    scaledArea,
    slices.toStream.zipWithIndex.map({ case (s, i) => (padding + (i + 0.5) * widthPerSlice, string(s)) }).toList,
    normalFont,
    false,
    labelAngle,
    black)

  val yTics = YTics(scaledArea, implicitly[Tics[Y]].tics(minY, maxY), normalFont, black)

  val bars = slices.toStream.zipWithIndex.zip(colorStream).map({
    case ((s, i), color) => {
      val leftX = padding + (whiteSpace / 2d) + i * widthPerSlice
      val rightX = leftX + (widthPerSlice * barWidthPercent)
      Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, dataView.valueOf(data, s)), fillColor = Some(color))
    }
  })

}
