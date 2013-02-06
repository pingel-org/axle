package axle.visualize

import collection._
import java.awt.Color
import Color._

case class ReactivePlot[X: Plottable, Y: Plottable](
  dataB: Behavior[Unit, Seq[(String, SortedMap[X, Y])]],
  connect: Boolean = true,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  pointDiameter: Int = 4,
  keyLeftPadding: Int = 20,
  keyTopPadding: Int = 50,
  keyWidth: Int = 80,
  fontName: String = "Courier New",
  fontSize: Int = 12,
  titleFontName: String = "Palatino",
  titleFontSize: Int = 20,
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow),
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxis: X,
  yAxisLabel: Option[String] = None) {

  def xPlottable(): Plottable[X] = implicitly[Plottable[X]]

  def yPlottable(): Plottable[Y] = implicitly[Plottable[Y]]

}
