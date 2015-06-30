package axle.visualize.element

import axle.visualize.BarChart
import axle.visualize.DataView

case class BarChartKey[S, Y, D](
    chart: BarChart[S, Y, D],
    title: Option[String])(
        implicit dataView: DataView[S, Y, D]) {

  val slices = dataView.keys(chart.initialValue)

}
