package axle.visualize

import cats.kernel.Order

import cats.Show
import axle.quanta.UnittedQuantity
import axle.quanta.Angle
import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.Text

case class BarChartGrouped[G, S, Y, D, H](
    dataFn: Function0[D],
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
    labelAngle: Option[UnittedQuantity[Angle, Double]] = Some(36d *: angleDouble.degree),
    colorOf: (G, S) => Color,
    hoverOf: (G, S) => Option[H] = (g: G, s: S) => None,
    linkOf: (G, S) => Option[(java.net.URL, Color)] = (g: G, s: S) => None)(
        implicit val showG: Show[G],
        val showS: Show[S],
        val showH: Show[H],
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

  // val colorStream = continually(colors.toStream).flatten
  val titleText = title.map(Text(_, width / 2d, titleFontSize, titleFontName, titleFontSize, bold = true))
  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2d, height - border / 2d, normalFontName, normalFontSize, bold = true))
  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2d, normalFontName, normalFontSize, bold = true, angle = Some(90d *: angleDouble.degree)))

}
