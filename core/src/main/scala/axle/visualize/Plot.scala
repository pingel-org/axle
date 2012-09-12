package axle.visualize

import scala.collection.Seq
import scala.collection.SortedMap

case class Plot[X, DX, Y, DY](
  lfs: Seq[(String, SortedMap[X, Y])],
  connect: Boolean = true,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  pointDiameter: Int = 4,
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxis: X,
  yAxisLabel: Option[String] = None)(
    implicit _xPlottable: Plottable[X], _yPlottable: Plottable[Y]) {

  val clockwise90 = math.Pi / -2.0
  val counterClockwise90 = -1.0 * clockwise90

  val minX = List(yAxis, lfs.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
  val maxX = List(yAxis, lfs.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
  val minY = List(xAxis, lfs.map(_._2.values.min(yPlottable)).min(yPlottable)).min(yPlottable)
  val maxY = List(xAxis, lfs.map(_._2.values.max(yPlottable)).max(yPlottable)).max(yPlottable)

  val xTics = xPlottable.tics(minX, maxX)
  val yTics = yPlottable.tics(minY, maxY)

  def xPlottable(): Plottable[X] = _xPlottable

  def yPlottable(): Plottable[Y] = _yPlottable

}
