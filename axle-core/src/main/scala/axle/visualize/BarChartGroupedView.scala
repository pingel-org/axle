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
    border.toDouble, (if (drawKey) width - (keyWidth + keyLeftPadding) else width) - border.toDouble,
    border.toDouble, height - (if (labelAngle.isDefined) border.toDouble else 5d),
    minX, maxX,
    minY, maxY)

  val vLine = VerticalLine(scaledArea, yAxis, black)
  val hLine = HorizontalLine(scaledArea, xAxis.getOrElse(zeroY.zero), black)

  val gTics = XTics(
    scaledArea,
    groups.toStream.zipWithIndex.map({ case (g, i) => (padding + (i + 0.5) * widthPerGroup, string(g)) }).toList,
    normalFontName,
    normalFontSize.toDouble,
    bold = true,
    drawLines = false,
    labelAngle,
    black)

  val yTics = YTics(scaledArea, Tics[Y].tics(minY, maxY), normalFontName, normalFontSize.toDouble, true, black)

  val barSliceWidth = (widthPerGroup - (whiteSpace / 2d)) / slices.size.toDouble

  val bars = for {
    (s, i) <- slices.toVector.zipWithIndex
    (g, j) <- groups.toStream.zipWithIndex
  } yield {
    val leftX = padding + (whiteSpace / 2d) + j * widthPerGroup + i * barSliceWidth
    val rightX = leftX + barSliceWidth
    val y = groupedDataView.valueOf(data, (g, s))

    val rectBase = Rectangle(
      scaledArea,
      Point2D(leftX, minY),
      Point2D(rightX, y),
      Option(colorOf(g, s)),
      id = Some((groups.size * i + j).toString))

    val hovered = hoverOf(g, s).map {
      case (hover) => {
        val hoverString = string(hover)
        rectBase.copy(hoverText = Some(hoverString))
      }
    } getOrElse { rectBase }

    linkOf(g, s) map {
      case (url, color) =>
        hovered.copy(link = Some((url, color)))
    } getOrElse { hovered }

  }

}
