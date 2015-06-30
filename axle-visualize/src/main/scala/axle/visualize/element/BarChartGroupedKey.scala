package axle.visualize.element

import axle.visualize.BarChartGrouped
import axle.visualize.GroupedDataView

case class BarChartGroupedKey[G, S, Y, D](
    chart: BarChartGrouped[G, S, Y, D],
    title: Option[String])(
        implicit groupedDataView: GroupedDataView[G, S, Y, D]) {

  val slices =
    groupedDataView.slices(chart.initialValue)

}
