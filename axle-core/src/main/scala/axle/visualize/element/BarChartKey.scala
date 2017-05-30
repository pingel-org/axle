package axle.visualize.element

import axle.visualize.BarChart
import axle.visualize.DataView

case class BarChartKey[C, Y, D, H](
    chart: BarChart[C, Y, D, H],
    title: Option[String])(
        implicit dataView: DataView[C, Y, D]) {

  val slices = dataView.keys(chart.dataFn.apply)

}
