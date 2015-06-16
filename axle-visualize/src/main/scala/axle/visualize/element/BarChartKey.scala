package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.algebra.Plottable
import axle.visualize.BarChart

case class BarChartKey[S, Y: Plottable, D](
    chart: BarChart[S, Y, D],
    font: Font,
    colorStream: Stream[Color]) {

  val slices = chart.dataView.keys(chart.initialValue)

}
