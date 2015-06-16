package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.visualize.ScaledArea2D

case class YTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  font: Font,
  color: Color = Color.black)
