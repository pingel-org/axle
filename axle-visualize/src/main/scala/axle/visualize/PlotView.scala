package axle.visualize

import java.awt.Color.black
import java.awt.Font

import scala.Stream.continually
import scala.collection.immutable.SortedMap

import akka.actor.ActorSystem
import axle.algebra.Plottable
import axle.quanta.Angle._
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Eq
import spire.math.Number.apply

class PlotView[X: Plottable: Eq, Y: Plottable: Eq](plot: Plot[X, Y], data: Seq[(String, SortedMap[X, Y])], normalFont: Font)(implicit systemOpt: Option[ActorSystem]) {

  import plot._

  val colorStream = continually(colors.toStream).flatten

  val xPlottable = implicitly[Plottable[X]]
  val yPlottable = implicitly[Plottable[Y]]

  val keyOpt = if (drawKey) {
    Some(new Key(plot, normalFont, colorStream, keyWidth, keyTopPadding, data))
  } else {
    None
  }

  val minXCandidates = (data collect { case (_, m) if m.size > 0 => m.firstKey }) ++ yAxis.toList
  val minX = if (minXCandidates.size > 0) minXCandidates.min else xPlottable.zero

  val minYCandidates = ((data collect { case (_, m) if m.size > 0 => m.values min }) ++ xAxis.toList) filter { yPlottable.isPlottable }
  val minY = if (minYCandidates.size > 0) minYCandidates.min else yPlottable.zero

  val maxXCandidates = (data collect { case (_, m) if m.size > 0 => m.lastKey }) ++ yAxis.toList
  val maxX = if (maxXCandidates.size > 0) maxXCandidates.max else xPlottable.zero

  val maxYCandidates = ((data collect { case (_, m) if m.size > 0 => m.values max }) ++ xAxis.toList) filter { yPlottable.isPlottable }
  val maxY = if (maxYCandidates.size > 0) maxYCandidates.max else yPlottable.zero

  val minPoint = Point2D(minX, minY)
  val maxPoint = Point2D(maxX, maxY)

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height, border,
    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y)

  val vLine = new VerticalLine(scaledArea, yAxis.getOrElse(minX), black)
  val hLine = new HorizontalLine(scaledArea, xAxis.getOrElse(minY), black)
  val xTics = new XTics(scaledArea, xPlottable.tics(minX, maxX), normalFont, true, 0 *: °, black)
  val yTics = new YTics(scaledArea, yPlottable.tics(minY, maxY), normalFont, black)

  val dataLines = new DataLines(scaledArea, data, colorStream, pointDiameter, connect)

}
