package axle.visualize

import axle.algebra.Tics
import axle.string
import axle.visualize.Color.black
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

case class BarChartView[C, Y, D, H](
    chart: BarChart[C, Y, D, H],
    data: D) {

  import chart._

  val minX = 0d
  val maxX = 1d
  val yAxis = minX

  val slices = dataView.keys(data)

  val padding = 0.05 // on each side
  val widthPerSlice = (1d - (2 * padding)) / slices.size
  val whiteSpace = widthPerSlice * (1d - barWidthPercent)

  val (dataMinY, dataMaxY) = dataView.yRange(data)

  val minY = List(xAxis.getOrElse(zeroY.zero), dataMinY).min
  val maxY = List(xAxis.getOrElse(zeroY.zero), dataMaxY).max

  implicit val ddls = axle.algebra.LengthSpace.doubleDoubleLengthSpace

  val scaledArea = ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY)

  val vLine = VerticalLine(scaledArea, yAxis, black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(zeroY.zero), black)

  val gTics = XTics(
    scaledArea,
    slices.toStream.zipWithIndex.map({ case (s, i) => (padding + (i + 0.5) * widthPerSlice, string(s)) }).toList,
    normalFontName,
    normalFontSize,
    bold = true,
    drawLines = false,
    labelAngle,
    black)

  val yTics = YTics(scaledArea, Tics[Y].tics(minY, maxY), normalFontName, normalFontSize, true, black)

  val bars = slices.toStream.zipWithIndex.map({
    case (c, i) => {
      val y = dataView.valueOf(data, c)
      val color = colorOf(c)
      val leftX = padding + (whiteSpace / 2d) + i * widthPerSlice
      val rightX = leftX + (widthPerSlice * barWidthPercent)
      val y0 = zeroY.zero
      val hoverOpt = hoverOf(c)
      hoverOpt.map { case (hover) =>
        val hoverString = string(hover)
        if (y >= y0) {
          Rectangle(scaledArea, Point2D(leftX, y0), Point2D(rightX, y), fillColor = Some(color), id = Some((i.toString, hoverString)))
        } else {
          Rectangle(scaledArea, Point2D(leftX, y), Point2D(rightX, y0), fillColor = Some(color), id = Some((i.toString, hoverString)))
        }
      } getOrElse {
        if (y >= y0) {
          Rectangle(scaledArea, Point2D(leftX, y0), Point2D(rightX, y), fillColor = Some(color))
        } else {
          Rectangle(scaledArea, Point2D(leftX, y), Point2D(rightX, y0), fillColor = Some(color))
        }
      }
    }
  })

}
