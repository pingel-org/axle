package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Color.black
import axle.visualize.ScaledArea2D

case class VerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v:          X,
  color:      Color              = black)
