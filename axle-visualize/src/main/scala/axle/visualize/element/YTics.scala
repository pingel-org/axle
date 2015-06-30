package axle.visualize.element

import java.awt.Color

import axle.visualize.ScaledArea2D

case class YTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  fontName: String,
  fontSize: Int,
  color: Color = Color.black)
