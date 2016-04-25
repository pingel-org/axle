package axle.visualize.element

import axle.visualize.Color
import axle.visualize.ScaledArea2D
import axle.visualize.ScatterDataView

case class DataPoints[X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: D,
  pointDiameter: Double)(
    implicit val dataView: ScatterDataView[X, Y, D])
