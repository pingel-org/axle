package axle.visualize.element

import axle.visualize.Color
import axle.visualize.ScaledArea2D

case class DataLines[S, X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(S, D)],
  orderedXs: D => Traversable[X],
  x2y: (D, X) => Y,
  colorOf: S => Color,
  pointDiameter: Int,
  connect: Boolean = true)
