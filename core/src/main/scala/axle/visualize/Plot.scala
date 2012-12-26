package axle.visualize

import scala.collection.Seq
import scala.collection.SortedMap

case class Plot[X, Y](
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

  val minX = List(yAxis, lfs.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
  val maxX = List(yAxis, lfs.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
  val minY = List(xAxis, lfs.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
  val maxY = List(xAxis, lfs.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)

  val xTics = xPlottable.tics(minX, maxX)
  val yTics = yPlottable.tics(minY, maxY)

  def xPlottable(): Plottable[X] = _xPlottable

  def yPlottable(): Plottable[Y] = _yPlottable

}
