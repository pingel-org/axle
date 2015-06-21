package axle.visualize.element

import java.awt.Font

import axle.visualize.BarChart
import axle.visualize.DataView

case class BarChartKey[S, Y, D](
    chart: BarChart[S, Y, D],
    font: Font)(implicit dataView: DataView[S, Y, D]) {

  val slices = dataView.keys(chart.initialValue)

}
