package axle.visualize.element

import axle.visualize.Color
import axle.visualize.ScaledArea2D

case class DataPoints[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  data: Set[(X, Y)],
  colorStream: Stream[Color],
  pointDiameter: Double)
