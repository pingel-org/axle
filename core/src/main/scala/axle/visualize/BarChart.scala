package axle.visualize

import collection._

case class BarChart[X, Y](
  xs: Seq[X],
  numSeries: Int,
  barFn: (X, Int) => Y,
  labeller: X => String,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  barWidthPercent: Double = 0.80,
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxisLabel: Option[String] = None)(implicit _yPlottable: Plottable[Y]) {

  val minY = List(xAxis, (0 to numSeries).map(s => (xs.map(barFn(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)

  val maxY = List(xAxis, (0 to numSeries).map(s => (xs.map(barFn(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)

  val yTics = yPlottable.tics(minY, maxY)

  def yPlottable(): Plottable[Y] = _yPlottable

}
