package axle.visualize.element

import axle.visualize.BarChartGrouped
import axle.visualize.GroupedDataView

case class BarChartGroupedKey[G, S, Y, D, H](
    chart: BarChartGrouped[G, S, Y, D, H],
    title: Option[String])(
        implicit groupedDataView: GroupedDataView[G, S, Y, D]) {

  val data = chart.dataFn.apply

  val slices =
    groupedDataView.slices(data)

  val groups =
    groupedDataView.groups(data)

}
