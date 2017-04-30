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

case class BarChartGroupedView[G, S, Y, D, H](
    chart: BarChartGrouped[G, S, Y, D, H],
    data: D) {

  import chart._

  val minX = 0d
  val maxX = 1d
  val yAxis = minX

  val groups = groupedDataView.groups(data)
  val slices = groupedDataView.slices(data)

  val padding = 0.05 // on each side
  val widthPerGroup = (1d - (2 * padding)) / groups.size
  val whiteSpace = widthPerGroup * (1d - barWidthPercent)

  val (dataMinY, dataMaxY) = groupedDataView.yRange(data)

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
    groups.toStream.zipWithIndex.map({ case (g, i) => (padding + (i + 0.5) * widthPerGroup, string(g)) }).toList,
    normalFontName,
    normalFontSize,
    bold = true,
    drawLines = false,
    36d *: angleDouble.degree,
    black)

  val yTics = YTics(scaledArea, Tics[Y].tics(minY, maxY), normalFontName, normalFontSize, true, black)

  val barSliceWidth = (widthPerGroup - (whiteSpace / 2d)) / slices.size.toDouble

  val bars = for {
    (s, j) <- slices.toVector.zipWithIndex
    (g, i) <- groups.toStream.zipWithIndex
  } yield {
    val leftX = padding + (whiteSpace / 2d) + i * widthPerGroup + j * barSliceWidth
    val rightX = leftX + barSliceWidth
    val y = groupedDataView.valueOf(data, (g, s))
    hoverOf(g, s).map {
      case (hover, hoverTextColor) => {
        val hoverString = string(hover)
        Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, y), Option(colorOf(g, s)), id = Some((i.toString, hoverString, hoverTextColor)))
      }
    } getOrElse {
      Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, y), Option(colorOf(g, s)))
    }
  }

}
