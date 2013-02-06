package axle.visualize

import collection._

case class BarChart[X, S, Y : Plottable](
  xs: Seq[X],
  ss: Seq[S],
  y: (X, S) => Y,
  xLabeller: X => String = (x: X) => x.toString,
  sLabeller: S => String = (s: S) => s.toString,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  barWidthPercent: Double = 0.80,
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxisLabel: Option[String] = None) {

  val minY = List(xAxis, ss.map(s => (xs.map(y(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)

  val maxY = List(xAxis, ss.map(s => (xs.map(y(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)

  val yTics = yPlottable.tics(minY, maxY)

  def yPlottable(): Plottable[Y] = implicitly[Plottable[Y]]

}
