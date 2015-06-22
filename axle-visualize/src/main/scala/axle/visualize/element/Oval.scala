package axle.visualize.element

import java.awt.Color

import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

case class Oval[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  center: Point2D[X, Y],
  width: Int,
  height: Int,
  color: Color,
  borderColor: Color)
