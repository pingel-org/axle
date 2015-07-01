package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Color.black
import axle.visualize.ScaledArea2D

case class YTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  fontName: String,
  fontSize: Int,
  color: Color = black)
