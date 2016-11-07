package axle.visualize

import scala.Stream.continually

import spire.algebra.Order

import cats.Show
import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.Text

case class BarChartGrouped[G, S, Y, D](
    initialValue: D,
    drawKey: Boolean = true,
    width: Int = 700,
    height: Int = 600,
    border: Int = 50,
    barWidthPercent: Double = 0.80,
    keyLeftPadding: Int = 20,
    keyTopPadding: Int = 50,
    keyWidth: Int = 80,
    title: Option[String] = None,
    keyTitle: Option[String] = None,
    normalFontName: String = "Courier New",
    normalFontSize: Int = 12,
    titleFontName: String = "Palatino",
    titleFontSize: Int = 20,
    xAxis: Option[Y] = None,
    xAxisLabel: Option[String] = None,
    yAxisLabel: Option[String] = None,
    colors: Seq[Color] = defaultColors)(
        implicit val showG: Show[G],
        val showS: Show[S],
        val orderY: Order[Y],
        val zeroY: Zero[Y],
        val ticsY: Tics[Y],
        val lengthSpaceY: LengthSpace[Y, _, Double],
        val groupedDataView: GroupedDataView[G, S, Y, D]) {

  val keyOpt = if (drawKey) {
    Some(BarChartGroupedKey(this, keyTitle))
  } else {
    None
  }

  val colorStream = continually(colors.toStream).flatten
  val titleText = title.map(Text(_, width / 2, titleFontSize, titleFontName, titleFontSize, bold = true))
  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2, height - border / 2, normalFontName, normalFontSize, bold = true))
  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2, normalFontName, normalFontSize, bold = true, angle = Some(90d *: angleDouble.degree)))

}
