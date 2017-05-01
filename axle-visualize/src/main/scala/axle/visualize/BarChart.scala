package axle.visualize

import cats.Show
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.element.BarChartKey
import axle.visualize.element.Text
import cats.kernel.Eq
import cats.kernel.Order

case class BarChart[C, Y, D, H](
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
    labelAngle: Option[UnittedQuantity[Angle, Double]] = Some(36d *: angleDouble.degree),
    colorOf: C => Color = (c: C) => Color.blue,
    hoverOf: C => Option[H] = (c: C) => None
    )(
        implicit val showC: Show[C],
        val showH: Show[H],
        val zeroY: Zero[Y],
        val orderY: Order[Y],
        val ticsY: Tics[Y],
        val eqY: Eq[Y],
        val plottableY: Plottable[Y],
        val dataView: DataView[C, Y, D],
        val lengthSpaceY: LengthSpace[Y, _, Double]) {

  val titleText = title.map(Text(_, width / 2, titleFontSize, titleFontName, titleFontSize, bold = true))
  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2, height - border / 2, normalFontName, normalFontSize, bold = true))
  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2, normalFontName, normalFontSize, bold = true, angle = Some(90d *: angleDouble.degree)))

  val keyOpt = if (drawKey) {
    Some(BarChartKey(this, keyTitle))
  } else {
    None
  }
}
