package axle.visualize.element

import axle.visualize.Color
import axle.visualize.ScaledArea2D

case class DataPoints[X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: D,
  dataToDomain: D => Set[(X, Y)],
  colorOf: (X, Y) => Color,
  pointDiameter: Double)
