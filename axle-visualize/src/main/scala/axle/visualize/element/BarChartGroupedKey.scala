package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.algebra.Plottable
import axle.visualize.BarChartGrouped

case class BarChartGroupedKey[G, S, Y: Plottable, D](
    chart: BarChartGrouped[G, S, Y, D],
    font: Font,
    colorStream: Stream[Color]) {

  val slices = chart.groupedDataView.slices(chart.initialValue)

}
