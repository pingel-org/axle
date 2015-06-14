package axle.visualize.element

import java.awt.Color

import axle.visualize.ScaledArea2D

case class HorizontalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  h: Y,
  color: Color = Color.black)
