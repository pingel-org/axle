package axle.visualize.element

import axle.visualize.ScaledArea2D
import axle.visualize.ScatterDataView
import axle.visualize.Color

case class DataPoints[S, X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: D,
  diameterOf: (X, Y) => Double,
  colorOf: (X, Y) => Color,
  labelOf: (X, Y) => Option[(S, Boolean)])(
    implicit val dataView: ScatterDataView[X, Y, D])
