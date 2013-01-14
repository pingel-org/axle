package axle.visualize

import scala.collection.Seq
import scala.collection.SortedMap

case class UpdatingPlot[X, Y](
  lfsf: () => Seq[(String, SortedMap[X, Y])],
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

  def dataSnapshot() = lfsf()

  def boundsAndTics(data: Seq[(String, SortedMap[X, Y])]): (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]) = {
    val minX = List(yAxis, data.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
    val maxX = List(yAxis, data.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
    val minY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
    val maxY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)
    val xTics = xPlottable.tics(minX, maxX)
    val yTics = yPlottable.tics(minY, maxY)
    (Point2D(minX, minY), Point2D(maxX, maxY), xTics, yTics)
  }

  def xPlottable(): Plottable[X] = _xPlottable

  def yPlottable(): Plottable[Y] = _yPlottable
  
}
