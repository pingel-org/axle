package axle.visualize.element

import axle.visualize.Color
import axle.visualize.ScaledArea2D

case class DataLines[X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(String, D)],
  orderedXs: D => Traversable[X],
  x2y: (D, X) => Y,
  colorOf: String => Color,
  pointDiameter: Int,
  connect: Boolean = true)
