package axle.visualize

import java.awt.Color
import java.awt.Color.black
import java.awt.Font

import scala.reflect.ClassTag

import axle.algebra.Plottable
import axle.algebra.Plottable.DoublePlottable
import axle.quanta.Angle.{ ° => ° }
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.math.Number.apply

class BarChartGroupedView[G, S, Y: Plottable: Eq, D: ClassTag](chart: BarChartGrouped[G, S, Y, D], data: D, colorStream: Stream[Color], normalFont: Font) {

  import chart._

  val minX = 0d
  val maxX = 1d
  val yAxis = minX

  val groups = groupsFn(data)
  val slices = slicesFn(data)
  
  val padding = 0.05 // on each side
  val widthPerGroup = (1d - (2 * padding)) / groups.size
  val whiteSpace = widthPerGroup * (1d - barWidthPercent)

  val yPlottable = implicitly[Plottable[Y]]
  
  val minY = List(xAxis, slices.map(s => (groups.map(g => gs2y(data, (g, s))) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).min).min).min
  val maxY = List(xAxis, slices.map(s => (groups.map(g => gs2y(data, (g, s))) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).max).max).max

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY)

  val vLine = new VerticalLine(scaledArea, yAxis, black)
  val hLine = new HorizontalLine(scaledArea, xAxis, black)

  val gTics = new XTics(
    scaledArea,
    groups.toStream.zipWithIndex.map({ case (g, i) => (padding + (i + 0.5) * widthPerGroup, gLabeller(g)) }).toList,
    normalFont,
    false,
    36 *: °,
    black)

  val yTics = new YTics(scaledArea, yPlottable.tics(minY, maxY), normalFont, black)

  val barSliceWidth = (widthPerGroup - (whiteSpace / 2d)) / slices.size.toDouble

  val bars = for {
    ((s, j), color) <- slices.toVector.zipWithIndex.zip(colorStream)
    (g, i) <- groups.toStream.zipWithIndex
  } yield {
    val leftX = padding + (whiteSpace / 2d) + i * widthPerGroup + j * barSliceWidth
    val rightX = leftX + barSliceWidth
    Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, gs2y(data, (g, s))), fillColor = Some(color))
  }

}