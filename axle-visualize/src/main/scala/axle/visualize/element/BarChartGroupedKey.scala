package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.algebra.Plottable
import axle.visualize.BarChartGrouped
import axle.visualize.GroupedDataView

case class BarChartGroupedKey[G, S, Y, D](
    chart: BarChartGrouped[G, S, Y, D],
    font: Font,
    colorStream: Stream[Color])(implicit groupedDataView: GroupedDataView[G, S, Y, D]) {

  val slices =
    groupedDataView.slices(chart.initialValue)

}
