package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.visualize.Plot

case class Key[X, Y, D](
  plot: Plot[X, Y, D],
  title: Option[String],
  font: Font,
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  data: Seq[(String, D)])
