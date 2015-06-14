package axle.visualize.element

import java.awt.Color

import axle.visualize.ScaledArea2D

case class VerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v: X,
  color: Color = Color.black)