package axle.visualize

import java.awt.Color
import Color._
import scala.collection.immutable.SortedMap
import axle.algebra.Plottable
import axle.quanta.Time

case class Plot[X: Plottable, Y: Plottable](
  initialValue: List[(String, SortedMap[X, Y])],
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
  xAxis: Option[Y] = None,
  xAxisLabel: Option[String] = None,
  yAxis: Option[X] = None,
  yAxisLabel: Option[String] = None,
  refresher: Option[(List[(String, SortedMap[X, Y])] => List[(String, SortedMap[X, Y])], Time.Q)] = None)
