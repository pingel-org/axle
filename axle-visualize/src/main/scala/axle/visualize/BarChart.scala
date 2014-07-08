package axle.visualize

import java.awt.Color
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow

import axle.algebra.Plottable
import axle.quanta.Angle
import axle.quanta.Angle.{° => °}
import spire.math.Number.apply

case class BarChart[S, Y: Plottable, D](
  initialValue: D,
  slicesFn: D => Traversable[S],
  s2y: (D, S) => Y,
  sLabeller: S => String = (s: S) => s.toString,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  barWidthPercent: Double = 0.80,
  keyLeftPadding: Int = 20,
  keyTopPadding: Int = 50,
  keyWidth: Int = 80,
  title: Option[String] = None,
  normalFontName: String = "Courier New",
  normalFontSize: Int = 12,
  titleFontName: String = "Palatino",
  titleFontSize: Int = 20,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxisLabel: Option[String] = None,
  labelAngle: Angle.Q = 36 *: °,
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow))
