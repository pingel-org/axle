package axle.visualize.element

import java.awt.Color

import axle.visualize.Plot

case class Key[X, Y, D](
  plot: Plot[X, Y, D],
  title: Option[String],
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  data: Seq[(String, D)])
