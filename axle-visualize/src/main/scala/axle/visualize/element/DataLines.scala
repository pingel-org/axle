package axle.visualize.element

import java.awt.Color

import axle.visualize.ScaledArea2D

case class DataLines[X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(String, D)],
  orderedXs: D => Traversable[X],
  x2y: (D, X) => Y,
  colorStream: Stream[Color],
  pointDiameter: Int,
  connect: Boolean = true)
